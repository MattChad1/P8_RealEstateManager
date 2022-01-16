package com.openclassrooms.realestatemanager.ui.detail_property

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import kotlinx.coroutines.launch

class DetailPropertyViewModel (private val repository: PropertyRepository,
                               private val typeOfPropertyRepository: TypeOfPropertyRepository) :
    ViewModel() {




    fun getPropertyById(id: Long): MutableLiveData<DetailPropertyViewState?> {
        var valueReturn: DetailPropertyViewState? = null
        val result = MutableLiveData<DetailPropertyViewState?>()

        viewModelScope.launch {

            val property = repository.getPropertyById(id)
            val allTypes: List<TypeOfProperty>? = typeOfPropertyRepository.getAllTypes()


            valueReturn = DetailPropertyViewState(
                property.id,
                allTypes?.first { it.id == property.id }?.name,
                100,
                property.squareFeet,
                property.rooms,
                property.bedrooms,
                property.bathrooms,
                property.description,
                property.photos,
                property.adress
            )
            result.postValue(valueReturn)
        }


                return result
    }


}