package com.openclassrooms.realestatemanager.ui.detail_property

import com.openclassrooms.realestatemanager.datas.model.Agent
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.Proximity

data class DetailPropertyViewState(
    var id: Int,
    var type: String,
    var agent: Agent,
    var price: Long,
    var squareFeet: Double? = null,
    var rooms: Int? = 0,
    var bedrooms: Int? = 0,
    var bathrooms: Int? = 0,
    var description: String? = null,
    var photos: MutableList<ImageRoom>? = null,
    var adress: String? = null,
    var proximities: List<Proximity>? = null,
    var dateStartSell: String, // format yyyy-mm-dd for easy sorting in sqlite
)