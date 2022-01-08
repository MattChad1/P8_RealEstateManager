package com.openclassrooms.realestatemanager.datas.database

import androidx.room.*
import com.openclassrooms.realestatemanager.datas.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(property: Property?): Long

    @Update
    fun update(property: Property?)

    @Query("SELECT * FROM Property")
    fun getAll(): Flow<List<Property?>?>

    @Query("SELECT * FROM Property WHERE id=:id")
    fun getPropertyById(id: Long): Property


}