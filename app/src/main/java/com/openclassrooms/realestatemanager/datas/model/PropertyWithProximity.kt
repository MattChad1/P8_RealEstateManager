package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PropertyWithProximity(
    @Embedded var property: Property,
    @Relation(
        parentColumn = "idProperty",
        entityColumn = "idProximity",
        associateBy = Junction(PropertyProximityCrossRef::class)
    )
    var proximities: List<Proximity>
)