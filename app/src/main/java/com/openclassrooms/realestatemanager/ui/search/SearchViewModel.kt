package com.openclassrooms.realestatemanager.ui.search

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.repository.FilterSearchRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val propertyRepository: PropertyRepository, private val filterSearchRepository: FilterSearchRepository) : ViewModel() {

    val filterLiveData = filterSearchRepository.filterLiveData

    private var allPropertiesLiveData = propertyRepository.allPropertiesComplete.asLiveData()
    val mediatorLiveData = MediatorLiveData<Int>()

    init {

        viewModelScope.launch {
            mediatorLiveData.addSource(allPropertiesLiveData) { value -> mediatorLiveData.setValue(countPropertiesWithFilter(value, filterLiveData.value)) }
            mediatorLiveData.addSource(filterLiveData) { filter ->
                mediatorLiveData.setValue(countPropertiesWithFilter(allPropertiesLiveData.value, filter))
            }


        }
    }


    companion object {
        const val PRICE = "price"
        const val NUMROOMS = "numrooms"
        const val NUMBEDROOMS = "numbedrooms"
        const val NUMBATHROOMS = "numbathrooms"
        const val SURFACE = "surface"
        const val PROXIMITY = "proximity"
        const val DATE_START_SALE = "dateStartSale"
        const val DATE_END_SALE = "dateEndSale"

    }

    fun updateFilter(field: String, valueLow: String?, valueHigh: String?) {

        var val1 = valueLow
        var val2 = valueHigh

        //Invert value in case user has put the lowest and the highest values in the wrong fields
        if (val1 != null && val2 != null && val1.toLong() > val2.toLong()) {
            val1 = valueHigh
            val2 = valueLow
        }


        when (field) {
            PRICE -> filterSearchRepository.filter.price = Pair(val1?.toLong(), val2?.toLong())
            NUMROOMS -> filterSearchRepository.filter.numRooms = Pair(val1?.toInt(), val2?.toInt())
            NUMBEDROOMS -> filterSearchRepository.filter.numBedrooms = Pair(val1?.toInt(), val2?.toInt())
            NUMBATHROOMS -> filterSearchRepository.filter.numBathrooms = Pair(val1?.toInt(), val2?.toInt())
            SURFACE -> filterSearchRepository.filter.surface = Pair(val1?.toInt(), val2?.toInt())
            PROXIMITY -> filterSearchRepository.filter.proximity = listOf()
            DATE_START_SALE -> filterSearchRepository.filter.dateStartSale = Pair(val1, val2)
            DATE_END_SALE -> filterSearchRepository.filter.dateStartSale = Pair(val1, val2)

        }
        filterSearchRepository.updateLiveData()

    }


    private fun countPropertiesWithFilter(allProperties: List<PropertyWithProximity>?, filter: Filter?): Int {
        if (allProperties == null) return 0
        if (filter == null || filter == Filter()) return allProperties.size

        var i = 0

        allProperties.forEach {
            if (filter.price.first != null) {
                if (it.property.price < filter.price.first!!) return@forEach
            }
            if (filter.price.second != null) {
                if (it.property.price > filter.price.second!!) return@forEach
            }
            if (filter.numRooms.first != null) {
                if (it.property.rooms == null && it.property.rooms!! < filter.price.first!!) return@forEach
            }
            if (filter.numRooms.second != null) {
                if (it.property.rooms == null && it.property.rooms!! > filter.price.second!!) return@forEach
            }

            i++

        }
        return i
    }
}