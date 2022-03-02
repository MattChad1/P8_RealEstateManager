package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.datas.model.*
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePropertyRepository: PropertyRepository {

    override fun getAllPropertiesComplete(): Flow<List<PropertyWithProximity>?> {
        return flow {
            emit(FakeDatas.fakePropertiesCompletes)
        delay(1000)
            emit(FakeDatas.fakePropertiesCompletes)
            delay(1000)
            emit(FakeDatas.fakePropertiesCompletes)
        }
    }

    override suspend fun insert(property: Property): Int {
        return 0
    }

    override suspend fun updateProperty(property: Property) {}


    override suspend fun getPropertyCompleteById(id: Int): PropertyWithProximity {
        return FakeDatas.fakePropertiesCompletes[0]
    }

    override suspend fun addPhoto(idProperty: Int, nameFile: String, legende: String, idBase: Int) {
    }

    override suspend fun deletePhoto(idProperty: Int) {
    }

    override suspend fun updateProximityForProperty(idProperty: Int, proximityIds: List<Int>) {
    }

    override suspend fun getMaxId(): Int = 1

    override suspend fun getAllProximities(): List<Proximity> {
        return FakeDatas.fakeProximities
    }

    override fun getAllTypes(): Flow<List<TypeOfProperty>> {
        return flow { emit(FakeDatas.fakeTypes) }
    }

    override suspend fun getAllAgents(): List<Agent> = FakeDatas.fakeAgents

    override suspend fun synchroniseRoomToFirestore() {
    }


}