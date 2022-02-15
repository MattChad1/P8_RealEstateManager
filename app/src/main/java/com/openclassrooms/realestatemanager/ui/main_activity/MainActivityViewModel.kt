package com.openclassrooms.realestatemanager.ui.main_activity

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository

class MainActivityViewModel(private val navigationRepository: NavigationRepository) : ViewModel() {


    fun getPreviousAdd(): Int? {
        val history = navigationRepository.propertiesConsultedIdsLiveData.value
        if (history == null || history.size<=1) return null
        history.removeLast()
        navigationRepository.propertiesConsultedIdsLiveData.value = history
        return history.last()
    }


}