package com.openclassrooms.realestatemanager.ui.add_property

import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import com.openclassrooms.realestatemanager.ui.list_properties.PropertyViewStateItem
import kotlinx.coroutines.launch

class AddPropertyViewModel (private val propertyRepository: PropertyRepository, private val typeOfPropertyRepository: TypeOfPropertyRepository) : ViewModel() {

    val TAG = "MyLog AddPropertyVM"
    var typesProperty : MutableLiveData<List<String>> = MutableLiveData<List<String>>()
//    init {
//        viewModelScope.launch {
//
//        }
//    }



    fun addNewProperty(type: TypeOfProperty,
                       agent: Int? = null,
                       price: Double? = null,
                       squareFeet: Double? = null,
                       rooms: Int? = 0,
                       bedrooms: Int? = 0,
                       bathrooms: Int? = 0,
                       description: String? = null,
                       photos: MutableList<ImageRoom>? = null,
                       adress: String? = null,
                       available: Boolean = false,
                       dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
                       dateSold: String? = null,
    ) {



    }

    val allTypes : LiveData<List<TypeOfProperty>> = typeOfPropertyRepository.allTypes.asLiveData()
//        Transformations.map(typeOfPropertyRepository.allTypes.asLiveData())
//        {it.map {it.nameType }}

//      fun getTypes(): LiveData<List<TypeOfProperty>> {
//          return typeOfPropertyRepository.allTypes.asLiveData()
//          viewModelScope.launch {
//              val allTypes = mutableListOf<String>()
//
//              typeOfPropertyRepository.allTypes.asLiveData().value?.let { Log.i(TAG, "getTypes: $it") }
//
//
//              typeOfPropertyRepository.allTypes.asLiveData().value?.forEach {
//                  allTypes.add(it.nameType)
//                  Log.i(TAG, "getTypes: " + it.nameType)
//              }
//              typesProperty.value = allTypes
//          }
////          return typesProperty
//    }


}