package com.openclassrooms.realestatemanager.ui.add_property

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.datas.model.InternalStoragePhoto
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.formatDateYearBefore
import com.openclassrooms.realestatemanager.utils.Utils.getTodayDate
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    init {
        viewModelScope.launch {
            maxId = propertyRepository.getMaxId() + 1
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
        dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
        dateSold: String? = null,
    ) {
        var idProperty = idEdit
        val dateStartSellFormatRoom = formatDateYearBefore(dateStartSell)
        val currentDate = formatDateYearBefore(getTodayDate())
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
                    dateStartSell,
                    dateSold
                )
                if (idEdit != 0) {
                    propertyRepository.updateProperty(newProperty)
                    propertyRepository.deletePhoto(idProperty)


                } else {
                    idProperty = propertyRepository.insert(
                        newProperty
                    )
                }



                for (i in imagesPrevLiveData.value!!) {
                    propertyRepository.addPhoto(idProperty, i.name, i.legend)
                }
            }

        }
    }

    fun addPhoto(nameFile: String, bmp: Bitmap, legend: String) {
        val photos = imagesPrevLiveData.value ?: mutableListOf()
        photos.add(InternalStoragePhoto(nameFile, bmp, legend))
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
                    property.proximities,
                    Utils.formatDateDayBefore(property.property.dateStartSell),
                    Utils.formatDateDayBefore(property.property.dateSold)
                )
            )
        }
        return result
    }

    suspend fun getMaxId(): Int {
        maxId = propertyRepository.getMaxId() + 1
        return maxId
    }


}






