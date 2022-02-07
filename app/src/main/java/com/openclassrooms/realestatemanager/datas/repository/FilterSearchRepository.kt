package com.openclassrooms.realestatemanager.datas.repository

import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.datas.model.Filter

class FilterSearchRepository {

    var filter = Filter()
    var filterLiveData = MutableLiveData<Filter>()

    init {
        filterLiveData.value = filter
    }

    fun updateLiveData () {filterLiveData.value = filter}


}