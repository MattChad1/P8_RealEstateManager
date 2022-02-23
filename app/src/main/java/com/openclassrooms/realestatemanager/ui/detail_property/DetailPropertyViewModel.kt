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
    val tempLiveData = MutableLiveData<DetailPropertyViewState?>()
    var propertyLiveData = MediatorLiveData<DetailPropertyViewState?>()
//    val propertyLiveData: LiveData<DetailPropertyViewState?> = Transformations.map(navigationRepository.propertiesConsultedIdsLiveData) { it ->
//       getLastProperty(it)
//
//    }

    init {
        propertyLiveData.addSource(navigationRepository.propertiesConsultedIdsLiveData) { it -> getLastProperty(it) }
        propertyLiveData.addSource(tempLiveData) { propertyLiveData.value = it }
    }


    fun getPropertyById(id: Int): DetailPropertyViewState? {
        var valueReturn: DetailPropertyViewState? = null
//        val result = MutableLiveData<DetailPropertyViewState?>()

        viewModelScope.launch {

            repository.getPropertyCompleteById(id).let { property ->
                valueReturn = DetailPropertyViewState(
                    property.property.idProperty,
                    property.typeOfProperty.nameType,
                    property.agent,
                    property.property.price,
                    property.property.squareFeet,
                    property.property.rooms,
                    property.property.bedrooms,
                    property.property.bathrooms,
                    property.property.description,
                    property.photos,
                    property.property.adress,
                    property.proximities,
                    property.property.dateStartSell,
                    property.property.dateSold
                )
        //                result.postValue(valueReturn)
                tempLiveData.value = valueReturn
            }
        }
        return valueReturn
    }

    fun getLastProperty(propertiesIds: List<Int>): DetailPropertyViewState? {
        val lastProperty = propertiesIds.last()
        return getPropertyById(lastProperty)

    }


}