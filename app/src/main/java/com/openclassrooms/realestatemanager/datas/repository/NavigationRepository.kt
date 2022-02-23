package com.openclassrooms.realestatemanager.datas.repository

import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.datas.model.Filter

class NavigationRepository {

    var filter = Filter()
    var filterLiveData = MutableLiveData<Filter>()
    var propertiesConsultedIdsLiveData = MutableLiveData<MutableList<Int>>()

    init {
        filterLiveData.value = filter
    }

    fun updateLiveData() {
        filterLiveData.value = filter
    }

    fun newPropertyConsulted(id: Int) {
        var values: MutableList<Int> = propertiesConsultedIdsLiveData.value ?: mutableListOf()
        if (values.lastOrNull() != id) values.add(id)
        propertiesConsultedIdsLiveData.value = values
    }


}