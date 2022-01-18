package com.openclassrooms.realestatemanager.datas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.datas.model.Agent

@Dao
interface AgentDao {

    @Insert
    suspend fun insert(agent: Agent?): Long

    @Update
    suspend fun update(Agent: Agent?)

    @Query("SELECT * FROM Agent")
    suspend fun getAll(): List<Agent?>?

    @Query("SELECT * FROM Agent WHERE idAgent=:id")
    suspend fun getAgentById(id: Long): Agent


}