package com.openclassrooms.realestatemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.datas.repository.FilterSearchRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import com.openclassrooms.realestatemanager.ui.add_property.AddPropertyViewModel
import com.openclassrooms.realestatemanager.ui.detail_property.DetailPropertyViewModel
import com.openclassrooms.realestatemanager.ui.list_properties.ListPropertiesViewModel
import com.openclassrooms.realestatemanager.ui.main_activity.MainActivityViewModel
import com.openclassrooms.realestatemanager.ui.search.SearchViewModel

class ViewModelFactory(private val propertyRepository: PropertyRepository, private val filterSearchRepository: FilterSearchRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(filterSearchRepository) as T
        } else if (modelClass.isAssignableFrom(ListPropertiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListPropertiesViewModel(propertyRepository, filterSearchRepository) as T
        } else if (modelClass.isAssignableFrom(AddPropertyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddPropertyViewModel(propertyRepository) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(propertyRepository, filterSearchRepository) as T
        } else if (modelClass.isAssignableFrom(DetailPropertyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailPropertyViewModel(propertyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}