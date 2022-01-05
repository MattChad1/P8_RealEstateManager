package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Proximity (
    @PrimaryKey
    val id: Int,
    val name: String
)