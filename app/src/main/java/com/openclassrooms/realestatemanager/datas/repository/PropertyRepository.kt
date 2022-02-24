package com.openclassrooms.realestatemanager.datas.repository

import com.openclassrooms.realestatemanager.datas.model.*
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

        fun getAllPropertiesComplete(): Flow<List<PropertyWithProximity>?>

        suspend fun insert(property: Property): Int

        suspend fun updateProperty(property: Property)

        suspend fun getPropertyCompleteById(id: Int): PropertyWithProximity

        suspend fun addPhoto(idProperty: Int, nameFile: String, legende: String)

        suspend fun deletePhoto(idProperty: Int)

        suspend fun updateProximityForProperty(idProperty: Int, proximityIds: List<Int>)

        suspend fun getMaxId(): Int

        suspend fun getAllProximities(): List<Proximity>

        fun getAllTypes(): Flow<List<TypeOfProperty>>

        suspend fun getAllAgents(): List<Agent>

        suspend fun synchroniseRoomToFirestore()

    }




