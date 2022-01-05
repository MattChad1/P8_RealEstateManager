package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.function.DoubleUnaryOperator

@Entity
data class Property(
    @PrimaryKey val id: Long,
    var type: String? = null,
    var agent: String? = null,
    var price: Int? = null,
    var squareFeet: Double? = null,
    var rooms: Int? = 0,
    var description: String? = null,
    var photo: MutableList<String?>? = null,
    var adress: String? = null,
    var proximity: MutableList<Int?>? = null,
    var status: Int = 0,
    var dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
    var dateSold: String? = null, // idem
)