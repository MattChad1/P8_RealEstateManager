package com.openclassrooms.realestatemanager.ui.add_property

import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.InternalStoragePhoto
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import com.openclassrooms.realestatemanager.ui.list_properties.PropertyViewStateItem
import kotlinx.coroutines.launch

class AddPropertyViewModel (private val propertyRepository: PropertyRepository, private val typeOfPropertyRepository: TypeOfPropertyRepository) : ViewModel() {

    val TAG = "MyLog AddPropertyVM"

    val allTypes : LiveData<List<TypeOfProperty>> = typeOfPropertyRepository.allTypes.asLiveData()
    var validAdress: MutableLiveData<String?> = MutableLiveData()
    var validPrice: MutableLiveData<String?> = MutableLiveData()
    var imagesPrev: MutableList<ImageRoom> = mutableListOf()

    fun addNewProperty(type: TypeOfProperty,
                       agent: Int? = null,
                       price: Int? = null,
                       squareFeet: Double? = null,
                       rooms: Int? = 0,
                       bedrooms: Int? = 0,
                       bathrooms: Int? = 0,
                       description: String? = null,
                       photos: MutableList<ImageRoom>? = null,
                       adress: String? = null,
                       dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
                       dateSold: String? = null,
    ) {


        validAdress.value = if (adress.isNullOrEmpty()) "Vous devez indiquer une adresse" else null
        validPrice.value = if (price == null) "Vous devez indiquer un prix" else null

        if (validAdress.value==null && validPrice.value==null) {
            viewModelScope.launch {
                val idInsert = propertyRepository.insert(
                    Property(
                        0,
                        type.idType,
                        adress!!,
                        description,
                        agent,
                        price,
                        squareFeet,
                        rooms,
                        bedrooms,
                        bathrooms,
                        photos,
                        dateStartSell,
                        dateSold
                    )
                )
                for (i in imagesPrev) {
                    propertyRepository.addPhoto(idInsert.toInt(), i.nameFile, i.legende)
                }
            }
            }
        }

    fun addPhoto(nameFile: String, legende: String ) {
        imagesPrev.add(ImageRoom(0, 0, nameFile, legende))

    }





}
