package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TypeOfProperty(
    @PrimaryKey(autoGenerate = false)
    var idType: Int,
    var nameType: String,

    ) {

    override fun toString(): String {
        return nameType
    }
}