package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.Timestamp.now
import java.sql.Date


@Entity
data class ImageRoom(@PrimaryKey(autoGenerate = true) var id: Int, var idProperty: Int = 0, var nameFile: String = "", var legende: String, var lastUpdate: Long = System.currentTimeMillis())