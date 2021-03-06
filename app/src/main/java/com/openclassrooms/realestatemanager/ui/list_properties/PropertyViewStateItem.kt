package com.openclassrooms.realestatemanager.ui.list_properties

import com.openclassrooms.realestatemanager.datas.model.ImageRoom

data class PropertyViewStateItem(
    var id: Int,
    var type: String,
    var price: Long,
    var adress: String,
    var squareFeet: Double? = null,
    var rooms: Int? = null,
    var bedrooms: Int? = null,
    var bathrooms: Int? = null,
    var description: String? = null,
    var proximitiesIds: List<Int>,
    var dateStartSale: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
    var dateSold: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
    var photo: ImageRoom? = null,
    var selected: Boolean = false,
)