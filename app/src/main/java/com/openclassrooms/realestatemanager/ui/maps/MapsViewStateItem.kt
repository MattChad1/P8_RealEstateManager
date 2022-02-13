package com.openclassrooms.realestatemanager.ui.maps

import com.openclassrooms.realestatemanager.datas.model.ImageRoom

data class MapsViewStateItem(
    var id: Int,
    var type: String,
    var price: Long,
    var photo: ImageRoom,
    var adress: String? = null,
)
