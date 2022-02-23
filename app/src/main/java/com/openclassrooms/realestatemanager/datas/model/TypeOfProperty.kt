package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TypeOfProperty(
    @PrimaryKey(autoGenerate = false)
    var idType: Int,
    var nameType: String,
    var lastUpdate: Long = System.currentTimeMillis()
) {
    constructor() : this(0, "")

    override fun toString(): String {
        return nameType
    }
}