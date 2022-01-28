package com.openclassrooms.realestatemanager.ui.list_properties

import com.openclassrooms.realestatemanager.datas.model.ImageRoom

data class PropertyViewStateItem (
    var id: Int,
    var type: String? = null,
    var price: Int? = null,
    var squareFeet: Double? = null,
    var rooms: Int? = 0,
    var bedrooms: Int? = 0,
    var bathrooms: Int? = 0,
    var description: String? = null,
    var photos: MutableList<ImageRoom>? = null,
    var adress: String? = null,
    var dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
        )