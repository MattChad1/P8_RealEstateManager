package com.openclassrooms.realestatemanager.ui.search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.datas.model.Filter
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import com.openclassrooms.realestatemanager.datas.model.Proximity
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val propertyRepository: PropertyRepository, private val navigationRepository: NavigationRepository) : ViewModel() {

    val filterLiveData = navigationRepository.filterLiveData

    private var allPropertiesLiveData = propertyRepository.getAllPropertiesComplete().asLiveData()
    val mediatorLiveData = MediatorLiveData<Int>()
    private var allProximities: MutableList<Proximity> = mutableListOf()

    init {
        viewModelScope.launch {
            mediatorLiveData.addSource(allPropertiesLiveData) { value -> mediatorLiveData.setValue(countPropertiesWithFilter(value, filterLiveData.value)) }
            mediatorLiveData.addSource(filterLiveData) { filter ->
                mediatorLiveData.setValue(countPropertiesWithFilter(allPropertiesLiveData.value, filter))
            }
        }
    }


    companion object {
        const val PRICE = "Price range"
        const val NUMROOMS = "Number of Rooms"
        const val NUMBEDROOMS = "Number of bedrooms"
        const val NUMBATHROOMS = "Number of bathrooms"
        const val SURFACE = "Surface range"
        const val PROXIMITY = "proximity"
        const val DATE_START_SALE = "In sale since"
        const val DATE_END_SALE = "Sold before"

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
            PRICE -> navigationRepository.filter.price = Pair(val1?.toLong(), val2?.toLong())
            NUMROOMS -> navigationRepository.filter.numRooms = Pair(val1?.toInt(), val2?.toInt())
            NUMBEDROOMS -> navigationRepository.filter.numBedrooms = Pair(val1?.toInt(), val2?.toInt())
            NUMBATHROOMS -> navigationRepository.filter.numBathrooms = Pair(val1?.toInt(), val2?.toInt())
            SURFACE -> navigationRepository.filter.surface = Pair(val1?.toInt(), val2?.toInt())
            DATE_START_SALE -> navigationRepository.filter.dateStartSale = val1
            DATE_END_SALE -> navigationRepository.filter.dateSoldMax = val1

        }
        navigationRepository.updateLiveData()

    }

    fun updateFilterProximity(proximitiesInts: MutableList<Int>) {
        navigationRepository.filter.proximity = proximitiesInts
    }

    fun resetFilter() {
        navigationRepository.filter = Filter()
        navigationRepository.updateLiveData()
    }


    private fun countPropertiesWithFilter(allProperties: List<PropertyWithProximity>?, filter: Filter?): Int {
        if (allProperties == null) return 0
        if (filter == null || filter == Filter()) return allProperties.size

        var i = 0

        allProperties.forEach { property ->

            // Filter price, min and max
            if (filter.price.first != null) {
                if (property.property.price < filter.price.first!!) return@forEach
            }
            if (filter.price.second != null) {
                if (property.property.price > filter.price.second!!) return@forEach
            }

            // Filter number of rooms, min and max
            if (filter.numRooms.first != null) {
                if (property.property.rooms == null || property.property.rooms!! < filter.numRooms.first!!) return@forEach
            }
            if (filter.numRooms.second != null) {
                if (property.property.rooms == null || property.property.rooms!! > filter.numRooms.second!!) return@forEach
            }

            // Filter number of bedrooms, min and max
            if (filter.numBedrooms.first != null) {
                if (property.property.bedrooms == null || property.property.bedrooms!! < filter.numBedrooms.first!!) return@forEach
            }
            if (filter.numBedrooms.second != null) {
                if (property.property.bedrooms == null || property.property.bedrooms!! > filter.numBedrooms.second!!) return@forEach
            }

            // Filter number of bathrooms, min and max
            if (filter.numBathrooms.first != null) {
                if (property.property.bathrooms == null || property.property.bathrooms!! < filter.numBathrooms.first!!) return@forEach
            }
            if (filter.numBathrooms.second != null) {
                if (property.property.bathrooms == null || property.property.bathrooms!! > filter.numBathrooms.second!!) return@forEach
            }

            // Filter surface, min and max
            if (filter.surface.first != null) {
                if (property.property.squareFeet == null || property.property.squareFeet!! < filter.surface.first!!) return@forEach
            }
            if (filter.surface.second != null) {
                if (property.property.squareFeet == null || property.property.squareFeet!! > filter.surface.second!!) return@forEach
            }

            // Start sale, only min
            if (filter.dateStartSale != null && property.property.dateStartSell < filter.dateStartSale!!) return@forEach

            // End sale, only max
            if (filter.dateSoldMax != null && property.property.dateSold != null && property.property.dateSold!! > filter.dateSoldMax!!) return@forEach

            // Proximities, contain
            val mapProximitiesId = property.proximities.map { it.idProximity }
            for (pSearch in filter.proximity) {
                if (pSearch !in mapProximitiesId) return@forEach
            }

            i++

        }
        return i
    }

    suspend fun getAllProximities(): MutableList<Proximity> {
        allProximities = propertyRepository.getAllProximities().toMutableList()
        return allProximities
    }
}