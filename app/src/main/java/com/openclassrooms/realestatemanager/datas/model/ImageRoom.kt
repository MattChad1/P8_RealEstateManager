package com.openclassrooms.realestatemanager.datas.model

import androidx.room.Entity
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
@Entity
data class ImageRoom (@PrimaryKey(autoGenerate = true) var id: Int, var idProperty: Int = 0, var nameFile: String = "", var legende: String)