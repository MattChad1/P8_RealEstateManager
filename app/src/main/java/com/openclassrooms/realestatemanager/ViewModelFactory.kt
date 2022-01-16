package com.openclassrooms.realestatemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.TypeOfPropertyRepository
import com.openclassrooms.realestatemanager.ui.add_property.AddPropertyViewModel
import com.openclassrooms.realestatemanager.ui.detail_property.DetailPropertyViewModel
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesViewModel

class ViewModelFactory (private val propertyRepository: PropertyRepository, private val typeOfPropertyRepository: TypeOfPropertyRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListPropertiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListPropertiesViewModel(propertyRepository, typeOfPropertyRepository) as T
        }
        else if (modelClass.isAssignableFrom(AddPropertyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddPropertyViewModel(propertyRepository, typeOfPropertyRepository) as T
        }

        else if (modelClass.isAssignableFrom(DetailPropertyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailPropertyViewModel(propertyRepository, typeOfPropertyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}