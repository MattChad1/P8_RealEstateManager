package com.openclassrooms.realestatemanager.datas.repository

import androidx.annotation.WorkerThread
import com.openclassrooms.realestatemanager.datas.database.LocaleDatabase
import com.openclassrooms.realestatemanager.datas.database.PropertyDao
import com.openclassrooms.realestatemanager.datas.model.Property
import kotlinx.coroutines.flow.Flow

class PropertyRepository (val propertyDao: PropertyDao) {

    val allProperties: Flow<List<Property?>?> = propertyDao.getAll()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(property: Property) {
        propertyDao.insert(property)
    }




}