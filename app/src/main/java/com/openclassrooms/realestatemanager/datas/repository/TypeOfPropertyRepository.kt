package com.openclassrooms.realestatemanager.datas.repository

import com.openclassrooms.realestatemanager.datas.database.TypeOfPropertyDao
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty

class TypeOfPropertyRepository (private val typeOfPropertyDao: TypeOfPropertyDao){



    suspend fun getAllTypes(): List<TypeOfProperty>? = typeOfPropertyDao.getAll()



}