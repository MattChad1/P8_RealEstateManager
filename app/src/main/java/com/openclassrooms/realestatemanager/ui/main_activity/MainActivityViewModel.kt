package com.openclassrooms.realestatemanager.ui.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class MainActivityViewModel(private val repository: PropertyRepository, private val navigationRepository: NavigationRepository) : ViewModel() {


    fun getPreviousAdd(): Int? {
        val history = navigationRepository.propertiesConsultedIdsLiveData.value
        if (history == null || history.size<=1) return null
        history.removeLast()
        navigationRepository.propertiesConsultedIdsLiveData.value = history
        return history.last()
    }

    fun synchroniseWithFirestore() {
        viewModelScope.launch(newSingleThreadContext("SynchroniseThread")) {
            repository.synchroniseRoomToFirestore()
        }
    }


}