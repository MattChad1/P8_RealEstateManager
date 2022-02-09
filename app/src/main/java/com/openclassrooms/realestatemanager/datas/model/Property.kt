package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Property(
    @PrimaryKey(autoGenerate = true)
    var idProperty: Int,
    var type: Int,
    var adress: String,
    var description: String? = null,
    var agent: Int,
    var price: Long,
    var squareFeet: Double? = null,
    var rooms: Int? = 0,
    var bedrooms: Int? = 0,
    var bathrooms: Int? = 0,
    var dateStartSell: String, // format yyyy-mm-dd for easy sorting in sqlite
    var dateSold: String? = null, // idem
)