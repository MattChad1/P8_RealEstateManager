package com.openclassrooms.realestatemanager.ui.maps

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.ui.list_properties.PropertyViewStateItem
import kotlinx.coroutines.launch

class MapsViewModel (val propertyRepository: PropertyRepository): ViewModel() {

    var markers = MutableLiveData<MapsViewStateItem>()
    val allPropertiesLiveData: LiveData<List<MapsViewStateItem>> =
        Transformations.map(propertyRepository.allPropertiesComplete.asLiveData(), ::filterProperty)


    private fun filterProperty(properties: List<PropertyWithProximity>?): List<MapsViewStateItem> {
        val newList = mutableListOf<MapsViewStateItem>()
        properties?.forEach {p ->
            if (p.property.dateSold == null)

                newList.add(
                    MapsViewStateItem(
                        p.property.idProperty,
                        p.typeOfProperty.nameType,
                        p.property.price,
                        p.photos[0],
                        p.property.adress

                    )
                )




        }
        return newList
    }
}

