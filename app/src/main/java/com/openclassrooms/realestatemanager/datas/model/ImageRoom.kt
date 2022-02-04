package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class ImageRoom (@PrimaryKey(autoGenerate = true) var id: Int, var idProperty: Int = 0, var nameFile: String = "", var legende: String)