package com.openclassrooms.realestatemanager.ui.list_properties

import com.openclassrooms.realestatemanager.datas.model.ImageRoom

data class PropertyViewStateItem(
    var id: Int,
    var type: String,
    var price: Long,
    var squareFeet: Double? = null,
    var rooms: Int? = null,
    var bedrooms: Int? = null,
    var bathrooms: Int? = null,
    var description: String? = null,
    var photo: ImageRoom,
    var adress: String? = null,
    var dateStartSale: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
    var sold: Boolean = false, // format yyyy-mm-dd for easy sorting in sqlite
)