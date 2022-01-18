package com.openclassrooms.realestatemanager.datas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty

@Dao
interface TypeOfPropertyDao {

    @Query("SELECT * FROM TypeOfProperty")
    suspend fun getAll(): List<TypeOfProperty>

    @Insert
    suspend fun insert(typeOfProperty: TypeOfProperty)
}