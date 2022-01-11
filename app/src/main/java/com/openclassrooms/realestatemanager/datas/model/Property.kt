package com.openclassrooms.realestatemanager.datas.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.datas.enumClass.ProximityEnum
import com.openclassrooms.realestatemanager.datas.enumClass.TypeEnum
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Property(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var type: TypeEnum? = null,
    var agent: Int? = null,
    var price: Int? = null,
    var squareFeet: Double? = null,
    var rooms: Int? = 0,
    var description: String? = null,
    var photo: MutableList<String?>? = null,
    var adress: String? = null,
    var proximity: MutableList<ProximityEnum>? = null,
    var available: Boolean = false,
    var dateStartSell: String? = null, // format yyyy-mm-dd for easy sorting in sqlite
    var dateSold: String? = null, // idem
)