package com.openclassrooms.realestatemanager.ui.add_property

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.datas.model.*
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.formatDateYearBefore
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream


class AddPropertyViewModel(private val propertyRepository: PropertyRepository, private val application: MyApplication = MyApplication.instance) : AndroidViewModel(application) {

    val TAG = "MyLog AddPropertyVM"



    val allTypes: LiveData<List<TypeOfProperty>> = propertyRepository.getAllTypes().asLiveData()

    var validAdress: MutableLiveData<String?> = MutableLiveData()
    var validPrice: MutableLiveData<String?> = MutableLiveData()
    var validImage: MutableLiveData<String?> = MutableLiveData()
    var validDateStartSell: MutableLiveData<String?> = MutableLiveData()
    var validAgent: MutableLiveData<String?> = MutableLiveData()
    var formFinished = MutableLiveData<Boolean>()


    var imagesPrevLiveData: MutableLiveData<MutableList<InternalStoragePhoto>> = MutableLiveData()
    var maxId: Int = 0
    var idEdit = 0
    var allProximities: MutableList<Proximity> = mutableListOf()
    var allAgents: MutableLiveData<List<Agent>> = MutableLiveData()

    init {
        viewModelScope.launch {
            maxId = propertyRepository.getMaxId() + 1
            allAgents.value = propertyRepository.getAllAgents()
        }
    }

    fun addNewProperty(
        type: TypeOfProperty,
        agent: Agent? = null,
        price: Long? = null,
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
        formFinished.value = false
        val dateStartSellFormatRoom = formatDateYearBefore(dateStartSell)

        val proximitiesForRoom = mutableListOf<Int>()
        if (!proximitiesSelected.isNullOrEmpty()) {
            proximitiesForRoom.addAll(allProximities.filter { proximitiesSelected.contains(it.idProximity) }.map { it.idProximity })
        }


        validAdress.value = if (adress.isNullOrEmpty()) application.getString(R.string.adress_required) else null
        validPrice.value = if (price == null) application.getString(R.string.price_required) else null
        validImage.value = if (imagesPrevLiveData.value.isNullOrEmpty()) application.getString(R.string.image_required) else null
        validDateStartSell.value = if (dateStartSellFormatRoom == null) application.getString(R.string.date_start_sale_required) else null
        validAgent.value = if (agent == null) application.getString(R.string.agent_required) else null

        if (validAdress.value == null && validPrice.value == null && validImage.value == null) {
            val job = viewModelScope.launch {
                val newProperty = Property(
                    idEdit,
                    type.idType,
                    adress!!,
                    description,
                    agent!!.idAgent,
                    price!!,
                    squareFeet,
                    rooms,
                    bedrooms,
                    bathrooms,
                    formatDateYearBefore(dateStartSell)!!,
                    formatDateYearBefore(dateSold)
                )

                if (idEdit != 0) {
                    propertyRepository.deletePhoto(idEdit)
                }

                idProperty = propertyRepository.insert(newProperty)

                for (i in imagesPrevLiveData.value!!) {
                    propertyRepository.addPhoto(idProperty, i.name, i.legend)
                }
                propertyRepository.updateProximityForProperty(idProperty, proximitiesForRoom)

            }
            viewModelScope.launch {
                job.join()
                formFinished.value = true
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


    suspend fun getAllProximities(): MutableList<Proximity> {
        allProximities = propertyRepository.getAllProximities().toMutableList()
        return allProximities
    }


}






