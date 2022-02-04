package com.openclassrooms.realestatemanager.ui.add_property

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.datas.model.*
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.formatDateYearBefore
import com.openclassrooms.realestatemanager.utils.Utils.getTodayDate
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream


class AddPropertyViewModel(private val propertyRepository: PropertyRepository, private val typeOfPropertyRepository: TypeOfPropertyRepository) : ViewModel() {

    val TAG = "MyLog AddPropertyVM"

    val allTypes: LiveData<List<TypeOfProperty>> = typeOfPropertyRepository.allTypes.asLiveData()
    var validAdress: MutableLiveData<String?> = MutableLiveData()
    var validPrice: MutableLiveData<String?> = MutableLiveData()
    var validImage: MutableLiveData<String?> = MutableLiveData()
    var validDateStartSell: MutableLiveData<String?> = MutableLiveData()
    var imagesPrevLiveData: MutableLiveData<MutableList<InternalStoragePhoto>> = MutableLiveData()
    var maxId: Int = 0
    var idEdit = 0
    var allProximities: MutableList<Proximity> = mutableListOf()

    init {
        viewModelScope.launch {
            maxId = propertyRepository.getMaxId() + 1
//            allProximities = propertyRepository.getAllProximities()

        }
    }

    fun addNewProperty(
        type: TypeOfProperty,
        agent: Int? = null,
        price: Int? = null,
        squareFeet: Double? = null,
        rooms: Int? = 0,
        bedrooms: Int? = 0,
        bathrooms: Int? = 0,
        description: String? = null,
        adress: String? = null,
        proximitiesSelected: List<Int>? = null,
        dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
        dateSold: String? = null,
    ) {
        var idProperty = idEdit
        val dateStartSellFormatRoom = formatDateYearBefore(dateStartSell)
        val currentDate = formatDateYearBefore(getTodayDate())

        var proximitiesForRoom = mutableListOf<Proximity>()
        if (!proximitiesSelected.isNullOrEmpty()) {
            proximitiesForRoom.addAll(allProximities.filter { proximitiesSelected.contains(it.idProximity)})
        }


        validAdress.value = if (adress.isNullOrEmpty()) "Vous devez indiquer une adresse" else null
        validPrice.value = if (price == null) "Vous devez indiquer un prix" else null
        validImage.value = if (imagesPrevLiveData.value.isNullOrEmpty()) "Vous devez choisir au moins une image" else null
        validDateStartSell.value = if (dateStartSellFormatRoom==null) "Vous devez saisir une date" else null

        if (validAdress.value == null && validPrice.value == null && validImage.value == null) {
            viewModelScope.launch {
                val newProperty = Property(
                    idEdit,
                    type.idType,
                    adress!!,
                    description,
                    agent,
                    price,
                    squareFeet,
                    rooms,
                    bedrooms,
                    bathrooms,
                    formatDateYearBefore(dateStartSell),
                    formatDateYearBefore(dateSold)
                )
                val images: MutableList<ImageRoom> = mutableListOf()

                if (idEdit != 0) {
//                    propertyRepository.updateProperty(newProperty)
                    propertyRepository.deletePhoto(idProperty)
                    propertyRepository.deleteProximityForProperty(idProperty)
                }
//                else {
//                    idProperty = propertyRepository.insert(
//                        newProperty
//                    )
//                }

                idProperty = propertyRepository.insert(newProperty)
                for (p in proximitiesForRoom) {
                    propertyRepository.insertPropertyProximityCrossRef(PropertyProximityCrossRef(idProperty, p.idProximity))
                }

                for (i in imagesPrevLiveData.value!!) {
                    images.add(ImageRoom(0, idProperty, i.name, i.legend))
                  propertyRepository.addPhoto(idProperty, i.name, i.legend)
                }


            }

        }
    }

    fun addPhoto(nameFile: String, bmp: Bitmap, legend: String) {
        val photos = imagesPrevLiveData.value ?: mutableListOf()
        if (InternalStoragePhoto(nameFile, bmp, legend) !in photos) photos.add(InternalStoragePhoto(nameFile, bmp, legend))
        imagesPrevLiveData.value = photos
    }

    fun addPhotoFromBase(img: ImageRoom) {
        val photos = imagesPrevLiveData.value ?: mutableListOf()

        val f = File(MyApplication.instance.filesDir, img.nameFile + ".jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))

        val imgInStorage: InternalStoragePhoto = InternalStoragePhoto(
            img.nameFile,
            b,
            img.legende
        )

        if (imgInStorage !in photos) photos.add(imgInStorage)
        imagesPrevLiveData.value = photos

    }


    fun checkLiveDataPhotos(photos: List<InternalStoragePhoto>) {
        val photosLiveData = imagesPrevLiveData.value ?: mutableListOf()
        for (p in photos) {
            if (p !in photosLiveData) photosLiveData.add(p)
        }
        imagesPrevLiveData.value = photosLiveData
    }


    fun getPropertyById(id: Int): MutableLiveData<EditPropertyViewState?> {
        val result = MutableLiveData<EditPropertyViewState?>()
        viewModelScope.launch {
            val property = propertyRepository.getPropertyCompleteById(id)
            result.postValue(
                EditPropertyViewState(
                    property.property.idProperty,
                    property.typeOfProperty,
                    property.property.price,
                    property.property.squareFeet,
                    property.property.rooms,
                    property.property.bedrooms,
                    property.property.bathrooms,
                    property.property.description,
                    property.photos,
                    property.property.adress,
                    property.proximities.map { it -> it.idProximity },
                    Utils.formatDateDayBefore(property.property.dateStartSell),
                    Utils.formatDateDayBefore(property.property.dateSold)
                )
            )
            property.photos?.forEach { p ->
                addPhotoFromBase(p)

            }

        }
        return result
    }

    suspend fun getMaxId(): Int {
        maxId = propertyRepository.getMaxId() + 1
        return maxId
    }

    suspend fun getAllProximities() :MutableList<Proximity> {
        allProximities = propertyRepository.getAllProximities().toMutableList()
        return allProximities
    }




}






