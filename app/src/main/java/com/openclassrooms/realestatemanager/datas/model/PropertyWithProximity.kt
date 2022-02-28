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
    var proximities: List<Proximity>,

    @Relation(
        parentColumn = "type",
        entityColumn = "idType",
        entity = TypeOfProperty::class

    )
    var typeOfProperty: TypeOfProperty,

    @Relation(
        parentColumn = "idProperty",
        entityColumn = "idProperty",
        entity = ImageRoom::class

    )
    var photos: MutableList<ImageRoom>?,

    @Relation(
        parentColumn = "agent",
        entityColumn = "idAgent",
        entity = Agent::class
    )
    var agent: Agent

)