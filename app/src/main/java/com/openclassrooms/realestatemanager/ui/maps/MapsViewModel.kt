package com.openclassrooms.realestatemanager.ui.maps

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository

class MapsViewModel(val propertyRepository: PropertyRepository, val navigationRepository: NavigationRepository) : ViewModel() {

    val allPropertiesLiveData: LiveData<List<MapsViewStateItem>> =
        Transformations.map(propertyRepository.getAllPropertiesComplete().asLiveData(), ::filterProperty)

    private fun filterProperty(properties: List<PropertyWithProximity>?): List<MapsViewStateItem> {
        val newList = mutableListOf<MapsViewStateItem>()
        properties?.forEach { p ->
            if (p.property.dateSold == null)
                newList.add(
                    MapsViewStateItem(
                        p.property.idProperty,
                        p.typeOfProperty.nameType,
                        p.property.price,
                        p.property.adress,
                        if (!p.photos.isNullOrEmpty()) p.photos!![0] else null
                    )
                )
        }
        return newList
    }

    fun changeSelection(id: Int) {
        navigationRepository.newPropertyConsulted(id)
    }
}

