package com.openclassrooms.realestatemanager.ui.add_property

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.datas.enumClass.ProximityEnum
import com.openclassrooms.realestatemanager.datas.enumClass.TypeEnum
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import kotlinx.coroutines.launch
import java.util.ArrayList

class AddPropertyViewModel (private val propertyRepository: PropertyRepository, private val typeOfPropertyRepository: TypeOfPropertyRepository) : ViewModel() {

    fun addNewProperty(type: TypeEnum? = null,
                       agent: Int? = null,
                       price: Int? = null,
                       squareFeet: Double? = null,
                       rooms: Int? = 0,
                       bedrooms: Int? = 0,
                       bathrooms: Int? = 0,
                       description: String? = null,
                       photos: MutableList<ImageRoom>? = null,
                       adress: String? = null,
                       proximity: MutableList<ProximityEnum>? = null,
                       available: Boolean = false,
                       dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
                       dateSold: String? = null,) {



    }


      fun getTypes(): List<String> {
          var list : List<String> = emptyList()
          viewModelScope.launch {
              val li: List<TypeOfProperty>? = typeOfPropertyRepository.getAllTypes()
              if (li != null) list = li.map { it.name }
          }
          return list
    }



}