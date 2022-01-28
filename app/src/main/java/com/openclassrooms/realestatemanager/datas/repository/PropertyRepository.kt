package com.openclassrooms.realestatemanager.datas.repository

import androidx.annotation.WorkerThread
import com.openclassrooms.realestatemanager.datas.database.ImageRoomDao
import com.openclassrooms.realestatemanager.datas.database.PropertyDao
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import kotlinx.coroutines.flow.Flow

class PropertyRepository (private val propertyDao: PropertyDao, private val imageRoomDao: ImageRoomDao) {

    val allPropertiesComplete: Flow<List<PropertyWithProximity>?> = propertyDao.getPropertiesComplete()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(property: Property): Long {
        return propertyDao.insert(property)
    }

    suspend fun getPropertyById(id: Long): Property = propertyDao.getPropertyById(id)

    suspend fun getPropertyCompleteById(id: Int): PropertyWithProximity = propertyDao.getPropertyCompleteById(id)

    suspend fun addPhoto(idProperty: Int, nameFile: String, legende: String ) {
        imageRoomDao.insert (ImageRoom(0, idProperty, nameFile, legende))
    }

    suspend fun getMaxId(): Int = propertyDao.getMaxId()


}