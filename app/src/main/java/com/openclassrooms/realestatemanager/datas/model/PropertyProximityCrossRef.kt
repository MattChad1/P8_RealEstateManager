package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity

@Entity(primaryKeys = ["idProperty", "idProximity"])
data class PropertyProximityCrossRef(
    var idProperty: Int,
    var idProximity: Int
)