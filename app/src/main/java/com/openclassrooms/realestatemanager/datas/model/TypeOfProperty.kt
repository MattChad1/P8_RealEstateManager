package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TypeOfProperty (
    @PrimaryKey(autoGenerate = false)
    var idType: Long,
    var nameType: String,

    )