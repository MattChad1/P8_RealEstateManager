package com.openclassrooms.realestatemanager.datas.repository

import androidx.annotation.WorkerThread
import com.openclassrooms.realestatemanager.datas.database.PropertyDao
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.model.PropertyComplete
import kotlinx.coroutines.flow.Flow

class PropertyRepository (private val propertyDao: PropertyDao) {

    val allProperties: Flow<List<Property?>?> = propertyDao.getAll()

    val allPropertiesComplete: Flow<List<PropertyComplete>?> = propertyDao.getPropertiesComplete()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(property: Property) {
        propertyDao.insert(property)
    }

    suspend fun getPropertyById(id: Long): Property = propertyDao.getPropertyById(id)

    suspend fun getPropertyCompleteById(id: Long): PropertyComplete = propertyDao.getPropertyCompleteById(id)




}