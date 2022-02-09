package com.openclassrooms.realestatemanager.datas.database

import androidx.room.*
import com.openclassrooms.realestatemanager.datas.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
    suspend fun addPhoto(imageRoom: ImageRoom)

    @Insert
    suspend fun insertPropertyProximityCrossRef(crossRef: PropertyProximityCrossRef)

    @Query("DELETE FROM ImageRoom WHERE idProperty=:idProperty")
    suspend fun deletePhoto(idProperty: Int)

    @Query("DELETE FROM PropertyProximityCrossRef WHERE idProperty=:idProperty")
    suspend fun deleteProximtyForProperty(idProperty: Int)

    @Query("SELECT idProperty FROM Property ORDER BY idProperty DESC LIMIT 1")
    suspend fun getMaxId(): Int

    @Query("SELECT * FROM Proximity")
    suspend fun getAllProximities(): List<Proximity>

    @Query("SELECT * FROM Agent")
    suspend fun getAgents(): List<Agent>


}