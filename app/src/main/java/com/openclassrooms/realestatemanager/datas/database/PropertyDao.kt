package com.openclassrooms.realestatemanager.datas.database

import androidx.room.*
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.model.PropertyWithProximity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: Property): Long

    @Update
    suspend fun update(property: Property?)

    @Query("SELECT * FROM Property")
    fun getAll(): Flow<List<Property?>?>

    @Query("SELECT * FROM Property WHERE idProperty=:id")
    suspend fun getPropertyById(id: Long): Property

    @Transaction
    @Query("SELECT * FROM Property")
    fun getPropertiesComplete(): Flow<List<PropertyWithProximity>?>

    @Transaction
    @Query("SELECT * FROM Property WHERE idProperty=:id")
    suspend fun getPropertyCompleteById(id: Int): PropertyWithProximity

    @Insert
    fun addPhoto(imageRoom: ImageRoom)

    @Query("SELECT idProperty FROM Property ORDER BY idProperty DESC LIMIT 1")
    suspend fun getMaxId(): Int


}