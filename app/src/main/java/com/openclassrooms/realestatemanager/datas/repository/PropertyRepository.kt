package com.openclassrooms.realestatemanager.datas.repository

import androidx.annotation.WorkerThread
import com.openclassrooms.realestatemanager.datas.database.ImageRoomDao
import com.openclassrooms.realestatemanager.datas.database.PropertyDao
import com.openclassrooms.realestatemanager.datas.model.*
import kotlinx.coroutines.flow.Flow

class PropertyRepository (private val propertyDao: PropertyDao, private val imageRoomDao: ImageRoomDao) {

    val allPropertiesComplete: Flow<List<PropertyWithProximity>?> = propertyDao.getPropertiesComplete()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(property: Property): Int {
        return propertyDao.insert(property).toInt()
    }

    suspend fun updateProperty(property: Property) = propertyDao.update(property)

    suspend fun getPropertyById(id: Int): PropertyWithProximity = propertyDao.getPropertyCompleteById(id)

    suspend fun getPropertyCompleteById(id: Int): PropertyWithProximity = propertyDao.getPropertyCompleteById(id)

    suspend fun addPhoto(idProperty: Int, nameFile: String, legende: String ) {
        imageRoomDao.insert (ImageRoom(0, idProperty, nameFile, legende))
    }

    suspend fun insertPropertyProximityCrossRef(crossRef: PropertyProximityCrossRef) {
        propertyDao.insertPropertyProximityCrossRef (crossRef)
    }

    suspend fun deletePhoto(idProperty: Int) {propertyDao.deletePhoto(idProperty)}
    suspend fun deleteProximityForProperty(idProperty: Int) {propertyDao.deleteProximtyForProperty(idProperty)}

    suspend fun getMaxId(): Int = propertyDao.getMaxId()

    suspend fun getAllProximities(): List<Proximity> {return propertyDao.getAllProximities()}


}