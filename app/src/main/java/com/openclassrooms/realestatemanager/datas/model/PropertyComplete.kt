package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Embedded
import androidx.room.Relation


data class PropertyComplete (
    @Embedded
    var property: Property,

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
        parentColumn = "idProperty",
        entityColumn = "idProximity",
        entity = Proximity::class
    )
    var proximities: List<Proximity>?



    )