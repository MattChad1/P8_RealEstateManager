package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.internal.LinkedTreeMap
import java.sql.Date

@Entity(primaryKeys = ["idProperty", "idProximity"])
data class PropertyProximityCrossRef(
    var idProperty: Int,
    var idProximity: Int,
    var lastUpdate: Long = System.currentTimeMillis()
) {
    constructor() : this(0, 0, 0)


}