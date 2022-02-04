package com.openclassrooms.realestatemanager.ui.add_property

import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.Proximity
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty

data class EditPropertyViewState(
    var id: Int,
    var type: TypeOfProperty,
    var price: Int? = null,
    var squareFeet: Double? = null,
    var rooms: Int? = 0,
    var bedrooms: Int? = 0,
    var bathrooms: Int? = 0,
    var description: String? = null,
    var photos: MutableList<ImageRoom>? = null,
    var adress: String? = null,
    var proximities: List<Int>? = null,
    var dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
    var dateSold: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
)
