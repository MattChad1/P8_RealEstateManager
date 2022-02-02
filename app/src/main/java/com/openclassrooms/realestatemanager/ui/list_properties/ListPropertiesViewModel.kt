package com.openclassrooms.realestatemanager.ui.list_properties

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import kotlinx.coroutines.launch

class ListPropertiesViewModel(
    private val repository: PropertyRepository,
    private val typeOfPropertyRepository: TypeOfPropertyRepository
) : ViewModel() {

    private val types = mutableListOf<TypeOfProperty>()

    init {
        viewModelScope.launch {
            typeOfPropertyRepository.allTypes.asLiveData().value?.let { types.addAll(it) }
        }
    }

    val allProperties: LiveData<List<PropertyViewStateItem>> =
        Transformations.map(repository.allPropertiesComplete.asLiveData(), ::displayProperty)


    private fun displayProperty(properties: List<PropertyWithProximity>?): List<PropertyViewStateItem> {
        var propertiesToReturn = mutableListOf<PropertyViewStateItem>()
        if (properties == null) return listOf()
        for (property in properties) {
            if (property.property.dateSold == null) {
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
                        property.photos,
                        property.property.adress
                    )
                )
            }
        }
        return propertiesToReturn
    }


}



