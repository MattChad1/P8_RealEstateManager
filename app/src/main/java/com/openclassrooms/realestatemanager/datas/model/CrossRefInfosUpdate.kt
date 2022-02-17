package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CrossRefInfosUpdate(
    @PrimaryKey
    val idProperty: Int,
    var lastUpdate: Long = System.currentTimeMillis()
) {

    constructor() : this(0)
}
