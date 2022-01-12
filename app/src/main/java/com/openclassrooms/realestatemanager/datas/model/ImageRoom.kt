package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
//@Entity (foreignKeys = [
//    ForeignKey(
//        entity = Property::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("idProperty"),
//        onDelete = ForeignKey.CASCADE
//    )],
//    indices = [
//        Index("idProperty")]
//)
data class ImageRoom (@PrimaryKey(autoGenerate = true) var id: Int, var idProperty: Int = 0, var src: String = "", var legende: String? = null)