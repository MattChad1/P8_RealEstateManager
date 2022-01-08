package com.openclassrooms.realestatemanager.ui.list_properties

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository

class ListPropertiesViewModel (private val repository: PropertyRepository) : ViewModel() {

    val allProperties: LiveData<List<Property?>?> = repository.allProperties.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
//    fun insert(word: Word) = viewModelScope.launch {
//        repository.insert(word)
//    }

}

class ViewModelFactory (private val propertyRepository: PropertyRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListPropertiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListPropertiesViewModel(propertyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}