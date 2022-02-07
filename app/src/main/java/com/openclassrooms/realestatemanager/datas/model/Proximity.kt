package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Proximity(
    @PrimaryKey(autoGenerate = false)
    var idProximity: Int,
    var nameProximity: String,
    var icon: String,
    var refLegend: String
)

