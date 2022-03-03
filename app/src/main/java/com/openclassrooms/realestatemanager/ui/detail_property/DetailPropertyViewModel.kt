package com.openclassrooms.realestatemanager.ui.detail_property

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.launch

class DetailPropertyViewModel(
    private val repository: PropertyRepository,
    private val navigationRepository: NavigationRepository
) :
    ViewModel() {
    private val tempLiveData = MutableLiveData<DetailPropertyViewState?>()
    var propertyLiveData = MediatorLiveData<DetailPropertyViewState?>()

    init {
        propertyLiveData.addSource(navigationRepository.propertiesConsultedIdsLiveData) { getLastProperty(it) }
        propertyLiveData.addSource(tempLiveData) { propertyLiveData.value = it }
    }


    private fun getPropertyById(id: Int): DetailPropertyViewState? {
        var valueReturn: DetailPropertyViewState? = null

        viewModelScope.launch {

            repository.getPropertyCompleteById(id).let { property ->
                valueReturn = DetailPropertyViewState(
                    property.property.idProperty,
                    property.typeOfProperty.nameType,
                    property.agent,
                    property.property.price,
                    property.property.adress,
                    property.property.squareFeet,
                    property.property.rooms,
                    property.property.bedrooms,
                    property.property.bathrooms,
                    property.property.description,
                    property.proximities,
                    property.property.dateStartSell,
                    property.property.dateSold,
                    if (!property.photos.isNullOrEmpty()) property.photos else null
                )
                //                result.postValue(valueReturn)
                tempLiveData.value = valueReturn
            }
        }
        return valueReturn
    }

    private fun getLastProperty(propertiesIds: List<Int>): DetailPropertyViewState? {
        val lastProperty = propertiesIds.last()
        return getPropertyById(lastProperty)
    }


}