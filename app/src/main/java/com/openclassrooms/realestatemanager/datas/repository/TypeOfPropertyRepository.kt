package com.openclassrooms.realestatemanager.datas.repository

import com.openclassrooms.realestatemanager.datas.database.TypeOfPropertyDao
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import kotlinx.coroutines.flow.Flow

class TypeOfPropertyRepository (private val typeOfPropertyDao: TypeOfPropertyDao){



    val allTypes: Flow<List<TypeOfProperty>> = typeOfPropertyDao.getAll()



}