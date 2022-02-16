package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp



@Entity
data class Agent(@PrimaryKey(autoGenerate = true) val idAgent: Int, val name: String, var lastUpdate: Long = System.currentTimeMillis() ) {

    override fun toString(): String {
        return name
    }
}