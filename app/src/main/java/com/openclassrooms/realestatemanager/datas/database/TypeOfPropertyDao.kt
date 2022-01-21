package com.openclassrooms.realestatemanager.datas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.datas.model.TypeOfProperty
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeOfPropertyDao {

    @Query("SELECT * FROM TypeOfProperty")
    fun getAll(): Flow<List<TypeOfProperty>>

    @Insert
    suspend fun insert(typeOfProperty: TypeOfProperty)
}