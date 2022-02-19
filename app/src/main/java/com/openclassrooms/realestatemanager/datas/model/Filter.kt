package com.openclassrooms.realestatemanager.datas.model

data class Filter(
    var price: Pair<Long?, Long?> = Pair(null, null),
    var numRooms: Pair<Int?, Int?> = Pair(null, null),
    var numBedrooms: Pair<Int?, Int?> = Pair(null, null),
    var numBathrooms: Pair<Int?, Int?> = Pair(null, null),
    var surface: Pair<Int?, Int?> = Pair(null, null),
    var proximity: MutableList<Int> = mutableListOf(),
    var dateStartSale: String? = null,
    var dateSoldMax:  String? = null,
)
