package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Proximity(
    @PrimaryKey(autoGenerate = false)
    var idProximity: Int,
    var nameProximity: String,
    var icon: String,
    var refLegend: String,
    var lastUpdate: Long = System.currentTimeMillis()
) {
    constructor() : this(0, "", "", "")

}

