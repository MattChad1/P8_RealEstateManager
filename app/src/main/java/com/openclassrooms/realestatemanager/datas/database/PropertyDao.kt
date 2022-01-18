package com.openclassrooms.realestatemanager.datas.database

import androidx.room.*
import com.openclassrooms.realestatemanager.datas.model.Property
import com.openclassrooms.realestatemanager.datas.model.PropertyComplete
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: Property?): Long

    @Update
    suspend fun update(property: Property?)

    @Query("SELECT * FROM Property")
    fun getAll(): Flow<List<Property?>?>

    @Query("SELECT * FROM Property WHERE idProperty=:id")
    suspend fun getPropertyById(id: Long): Property

    @Transaction
    @Query("SELECT * FROM Property")
    fun getPropertiesComplete(): Flow<List<PropertyComplete>?>

//    @Transaction
//    @Query("SELECT * FROM Property " +
//            "JOIN TypeOfProperty ON Property.idProperty = TypeOfProperty.idType " +
//            "JOIN PropertyProximityCrossRef ON Property.idProperty= PropertyProximityCrossRef.idProperty " +
//            "JOIN Proximity ON PropertyProximityCrossRef.idProximity = Proximity.idProximity")
//    fun getPropertiesComplete(): Flow<List<PropertyComplete>?>

    @Transaction
    @Query("SELECT * FROM Property WHERE idProperty=:id")
    suspend fun getPropertyCompleteById(id: Long): PropertyComplete


}