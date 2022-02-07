package com.openclassrooms.realestatemanager.datas.model

data class Filter(
    var price: Pair<Long?, Long?> = Pair(null, null),
    var numRooms: Pair<Int?, Int?> = Pair(null, null),
    var numBedrooms: Pair<Int?, Int?> = Pair(null, null),
    var numBathrooms: Pair<Int?, Int?> = Pair(null, null),
    var surface: Pair<Int?, Int?> = Pair(null, null),
    var proximity: List<Proximity> = listOf(),
    var dateStartSale: Pair<String?, String?> = Pair(null, null),
    var dateEndSale: Pair<String?, String?> = Pair(null, null),



)
