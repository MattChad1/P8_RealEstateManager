package com.openclassrooms.realestatemanager.ui.detail_property

import com.openclassrooms.realestatemanager.datas.model.ImageRoom

data class DetailPropertyViewState (
    var id: Long,
    var type: String? = null,
    var price: Int? = null,
    var squareFeet: Double? = null,
    var rooms: Int? = 0,
    var bedrooms: Int? = 0,
    var bathrooms: Int? = 0,
    var description: String? = null,
    var photos: MutableList<ImageRoom>? = null,
    var adress: String? = null,
    var proximity: List<Int>? = null,
    var dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
)