package com.openclassrooms.realestatemanager.ui.list_properties

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.datas.repository.FilterSearchRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.launch

class ListPropertiesViewModel(
    private val repository: PropertyRepository,
    private val filterSearchRepository: FilterSearchRepository
) : ViewModel() {

    private val types = mutableListOf<TypeOfProperty>()

    val allPropertiesLiveData: LiveData<List<PropertyViewStateItem>> =
        Transformations.map(repository.allPropertiesComplete.asLiveData(), ::displayProperty)

    val filterLiveData = filterSearchRepository.filterLiveData

    val mediatorLiveData: MediatorLiveData<List<PropertyViewStateItem>> = MediatorLiveData()

    init {
        viewModelScope.launch {
            repository.allTypes.asLiveData().value?.let { types.addAll(it) }
            mediatorLiveData.addSource(allPropertiesLiveData) { value ->
                mediatorLiveData.setValue(
                    filterListProperties(
                        allPropertiesLiveData.value,
                        filterLiveData.value
                    )
                )
            }
            mediatorLiveData.addSource(filterLiveData) { filter ->
                mediatorLiveData.setValue(filterListProperties(mediatorLiveData.value, filter))
            }
        }
    }


    private fun displayProperty(properties: List<PropertyWithProximity>?): List<PropertyViewStateItem> {
        var propertiesToReturn = mutableListOf<PropertyViewStateItem>()
        if (properties == null) return listOf()
        for (property in properties) {
                propertiesToReturn.add(
                    PropertyViewStateItem(
                        property.property.idProperty,
                        property.typeOfProperty.nameType,
                        property.property.price,
                        property.property.squareFeet,
                        property.property.rooms,
                        property.property.bedrooms,
                        property.property.bathrooms,
                        property.property.description,
                        property.photos[0],
                        property.property.adress,
                        property.proximities.map { it.idProximity },
                        property.property.dateStartSell,
                        property.property.dateSold
                    )
                )

        }
        return propertiesToReturn
    }

    private fun filterListProperties(properties: List<PropertyViewStateItem>?, filter: Filter?): List<PropertyViewStateItem>? {
        if (properties == null) return listOf<PropertyViewStateItem>()
        if (filter == null) return properties
        val newList = mutableListOf<PropertyViewStateItem>()


        properties?.forEach {
            // Filter price, min and max
            if (filter.price.first != null) {
                if (it.price < filter.price.first!!) return@forEach
            }
            if (filter.price.second != null) {
                if (it.price > filter.price.second!!) return@forEach
            }

            // Filter number of rooms, min and max
            if (filter.numRooms.first != null) {
                if (it.rooms == null || it.rooms!! < filter.numRooms.first!!) return@forEach
            }
            if (filter.numRooms.second != null) {
                if (it.rooms == null || it.rooms!! > filter.numRooms.second!!) return@forEach
            }

            // Filter number of bedrooms, min and max
            if (filter.numBedrooms.first != null) {
                if (it.bedrooms == null || it.bedrooms!! < filter.numBedrooms.first!!) return@forEach
            }
            if (filter.numBedrooms.second != null) {
                if (it.bedrooms == null || it.bedrooms!! > filter.numBedrooms.second!!) return@forEach
            }

            // Filter number of bathrooms, min and max
            if (filter.numBathrooms.first != null) {
                if (it.bathrooms == null || it.bathrooms!! < filter.numBathrooms.first!!) return@forEach
            }
            if (filter.numBathrooms.second != null) {
                if (it.bathrooms == null || it.bathrooms!! > filter.numBathrooms.second!!) return@forEach
            }

            // Filter surface, min and max
            if (filter.surface.first != null) {
                if (it.squareFeet == null || it.squareFeet!! < filter.surface.first!!) return@forEach
            }
            if (filter.surface.second != null) {
                if (it.squareFeet == null || it.squareFeet!! > filter.surface.second!!) return@forEach
            }

            // Start sale, only min
            if (filter.dateStartSale != null && it.dateStartSale!! < filter.dateStartSale!!) return@forEach

            // End sale, only max
            if (filter.dateSoldMax != null && it.dateSold !=null && it.dateSold!! > filter.dateSoldMax!!) return@forEach

            // Proximities, contain
            for (pSearch in filter.proximity) {
                if (pSearch !in it.proximitiesIds) return@forEach
            }

            newList.add(it)

        }
        return newList
    }


}



