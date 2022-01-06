package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Agent (@PrimaryKey(autoGenerate = true) val id: Int, val name: String)