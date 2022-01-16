package com.openclassrooms.realestatemanager.ui.list_properties

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.databinding.CardDatasDetailsBinding
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import com.openclassrooms.realestatemanager.ui.detail_property.DetailPropertyViewState
import com.openclassrooms.realestatemanager.ui.detail_property.ImagePropertyAdapter
import kotlinx.coroutines.launch

class ListPropertiesViewModel(
    private val repository: PropertyRepository,
    private val typeOfPropertyRepository: TypeOfPropertyRepository
) : ViewModel() {

//    val allTypes = mutableListOf<TypeOfProperty>()
//    init {
//        viewModelScope.launch {
//            val allTypes: List<TypeOfProperty>? = typeOfPropertyRepository.getAllTypes()
//        }
//    }

    val allProperties: LiveData<List<PropertyViewStateItem>> =
        Transformations.map(repository.allProperties.asLiveData(), ::displayProperty)


    private fun displayProperty(properties: List<Property?>?): List<PropertyViewStateItem> {
        var propertiesToReturn = mutableListOf<PropertyViewStateItem>()
        if (properties == null) return listOf<PropertyViewStateItem>()
        for (property in properties) {
            if (property != null) {
                propertiesToReturn.add(
                    PropertyViewStateItem(
                        property.id,
//                        typeOfPropertyRepository.getAllTypes()?.first { it.id == property.id }?.name,
                        "",
                        100,
                        property.squareFeet,
                        property.rooms,
                        property.bedrooms,
                        property.bathrooms,
                        property.description,
                        property.photos,
                        property.adress
                    )

                )

            }
        }
        return propertiesToReturn
    }


}



