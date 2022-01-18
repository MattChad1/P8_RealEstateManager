package com.openclassrooms.realestatemanager.datas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.openclassrooms.realestatemanager.datas.model.PropertyProximityCrossRef


@Dao
interface PropertyProximityCrossRefDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(p: PropertyProximityCrossRef)


}