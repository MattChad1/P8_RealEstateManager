package com.openclassrooms.realestatemanager.datas.database

import androidx.room.*
import com.openclassrooms.realestatemanager.datas.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {


    // Property Table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: Property): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProperties(properties: List<Property>)

    @Update
    suspend fun update(property: Property?)

    @Query("SELECT * FROM Property")
    fun getAllProperties(): Flow<List<Property>?>

    @Query("SELECT * FROM Property")
    fun getAllPropertiesNoFlow(): List<Property>?

    @Query("SELECT * FROM Property WHERE idProperty=:id")
    suspend fun getPropertyById(id: Long): Property

    @Query("SELECT idProperty FROM Property ORDER BY idProperty DESC LIMIT 1")
    suspend fun getMaxId(): Int

    ///////////////
    // Types table
    //////////////
    @Query("SELECT * FROM TypeOfProperty")
    fun getAllTypes(): Flow<List<TypeOfProperty>>

    @Query("SELECT * FROM TypeOfProperty")
    fun getAllTypesNoFlow(): List<TypeOfProperty>

    @Insert
    suspend fun insertType(typeOfProperty: TypeOfProperty)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTypes(types: List<TypeOfProperty>)

    ///////////////
    // ImageRoom table
    ///////////////
    @Insert
    suspend fun addPhoto(imageRoom: ImageRoom)

    @Query("DELETE FROM ImageRoom WHERE idProperty=:idProperty")
    suspend fun deletePhoto(idProperty: Int)

    @Query("SELECT * FROM ImageRoom")
    suspend fun getAllImages(): List<ImageRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllImages(images: List<ImageRoom>)


    ///////////////
    // Proximity table
    ///////////////
    @Query("SELECT * FROM Proximity")
    suspend fun getAllProximities(): List<Proximity>

    @Insert
    suspend fun insertProximity(proximity: Proximity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProximities(proximities: List<Proximity>)


    ///////////////
    // Agent table
    ///////////////
    @Query("SELECT * FROM Agent")
    suspend fun getAllAgents(): List<Agent>

    @Insert
    suspend fun insertAgent(agent: Agent?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAgents(agents: List<Agent>)

    ///////////////
    // Crossref
    ///////////////
    @Query("DELETE FROM PropertyProximityCrossRef WHERE idProperty=:idProperty")
    suspend fun deleteProximtyForProperty(idProperty: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyProximityCrossRef(crossRef: PropertyProximityCrossRef)

    @Query("SELECT * FROM PropertyProximityCrossRef")
    suspend fun getAllCrossRef(): List<PropertyProximityCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCrossRef(crossRefs: List<PropertyProximityCrossRef>)

    @Query("SELECT * FROM PropertyProximityCrossRef WHERE idProperty=:idProperty")
    suspend fun getCrossRefForProperty(idProperty: Int): List<PropertyProximityCrossRef>

    /////////
    //InfosUpdateCrossRef
    ////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCrossRefInfosUpdate(data: CrossRefInfosUpdate)

    @Query("SELECT * FROM CrossRefInfosUpdate")
    suspend fun getCrossRefInfosUpdate(): List<CrossRefInfosUpdate>


    ///////////////
    // Transactions
    ///////////////
    @Transaction
    @Query("SELECT * FROM Property")
    fun getPropertiesComplete(): Flow<List<PropertyWithProximity>?>

    @Transaction
    @Query("SELECT * FROM Property WHERE idProperty=:id")
    suspend fun getPropertyCompleteById(id: Int): PropertyWithProximity


}