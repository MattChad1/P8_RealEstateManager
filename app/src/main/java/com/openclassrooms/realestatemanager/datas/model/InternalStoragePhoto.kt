package com.openclassrooms.realestatemanager.datas.model

import android.graphics.Bitmap

data class InternalStoragePhoto(
    val name: String,
    val bmp: Bitmap,
    val legend: String = "",
    val idBase: Int = 0
)