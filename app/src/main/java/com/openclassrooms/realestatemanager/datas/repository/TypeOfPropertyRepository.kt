package com.openclassrooms.realestatemanager.datas.repository

import androidx.annotation.WorkerThread
import com.openclassrooms.realestatemanager.datas.database.TypeOfPropertyDao
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import kotlinx.coroutines.flow.Flow

class TypeOfPropertyRepository (private val typeOfPropertyDao: TypeOfPropertyDao){



    suspend fun getAllTypes(): List<TypeOfProperty>? = typeOfPropertyDao.getAll()



}