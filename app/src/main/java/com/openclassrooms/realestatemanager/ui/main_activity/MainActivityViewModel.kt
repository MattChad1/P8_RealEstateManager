package com.openclassrooms.realestatemanager.ui.main_activity

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class MainActivityViewModel(private val repository: PropertyRepository, private val navigationRepository: NavigationRepository) : ViewModel() {

    val countFilterLiveData = Transformations.map(navigationRepository.filterLiveData, ::countFilters)

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

    fun countFilters(filter: Filter): Int {
        var numFilter = 0
        if (filter.price != Pair(null, null)) numFilter +=1
        if (filter.numRooms != Pair(null, null)) numFilter +=1
        if (filter.numBedrooms != Pair(null, null)) numFilter +=1
        if (filter.numBathrooms != Pair(null, null)) numFilter +=1
        if (filter.surface != Pair(null, null)) numFilter +=1
        if (filter.proximity.isNotEmpty()) numFilter +=1
        if (filter.dateStartSale != null) numFilter +=1
        if (filter.dateSoldMax != null) numFilter +=1

        return numFilter
    }




}