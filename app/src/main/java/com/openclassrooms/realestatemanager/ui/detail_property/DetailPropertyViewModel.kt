package com.openclassrooms.realestatemanager.ui.detail_property

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import kotlinx.coroutines.launch

class DetailPropertyViewModel(
    private val repository: PropertyRepository
) :
    ViewModel() {


    fun getPropertyById(id: Int): MutableLiveData<DetailPropertyViewState?> {
        var valueReturn: DetailPropertyViewState? = null
        val result = MutableLiveData<DetailPropertyViewState?>()

        viewModelScope.launch {

            val property = repository.getPropertyCompleteById(id)


            valueReturn = DetailPropertyViewState(
                property.property.idProperty,
                property.typeOfProperty.nameType,
                property.property.price,
                property.property.squareFeet,
                property.property.rooms,
                property.property.bedrooms,
                property.property.bathrooms,
                property.property.description,
                property.photos,
                property.property.adress,
                property.proximities
            )
            result.postValue(valueReturn)
        }
        return result
    }


}