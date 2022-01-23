package com.openclassrooms.realestatemanager.datas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.datas.model.ImageRoom

@Dao
interface ImageRoomDao {

    @Insert
    suspend fun insert(imageRoom: ImageRoom?): Long

    @Insert
    fun insertAll(images: List<ImageRoom>?)

    @Update
    fun update(ImageRoom: ImageRoom?)

    @Query("SELECT * FROM ImageRoom")
    fun getAll(): List<ImageRoom?>?

    @Query("SELECT * FROM ImageRoom WHERE id=:id")
    fun getImageRoomById(id: Long): ImageRoom


}