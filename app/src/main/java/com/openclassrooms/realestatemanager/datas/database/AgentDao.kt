package com.openclassrooms.realestatemanager.datas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.datas.model.Agent

@Dao
interface AgentDao {

    @Insert
    fun insert(agent: Agent?): Long

    @Update
    fun update(Agent: Agent?)

    @Query("SELECT * FROM Agent")
    fun getAll(): List<Agent?>?

    @Query("SELECT * FROM Agent WHERE id=:id")
    fun getAgentById(id: Long): Agent


}