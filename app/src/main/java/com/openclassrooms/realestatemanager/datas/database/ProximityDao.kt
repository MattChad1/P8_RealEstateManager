package com.openclassrooms.realestatemanager.datas.database

import androidx.room.Dao
import androidx.room.Insert
import com.openclassrooms.realestatemanager.datas.model.Proximity

@Dao
interface ProximityDao {

    @Insert
    suspend fun insert(proximity: Proximity)

    @Insert
    suspend fun insertAll(proximities: List<Proximity>)


}