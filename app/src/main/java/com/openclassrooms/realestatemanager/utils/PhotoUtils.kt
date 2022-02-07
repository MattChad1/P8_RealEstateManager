package com.openclassrooms.realestatemanager.utils

import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.datas.model.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class PhotoUtils {

    companion object {
        fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {
            return try {
                MyApplication.instance.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream)) {
                        throw IOException("Couldn't save bitmap.")
                    }
                }
                true
            }
            catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }


        suspend fun loadPhotosFromInternalStorage(prefix: String = ""): List<InternalStoragePhoto> {
            return withContext(Dispatchers.IO) {
                val files = MyApplication.instance.filesDir.listFiles()
                files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") && it.name.startsWith(prefix) }?.map {
                    val bytes = it.readBytes()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    InternalStoragePhoto(it.name, bmp)
                } ?: listOf()
            }
        }

        fun deletePhotoFromInternalStorage(filename: String): Boolean {
            return try {
                MyApplication.instance.deleteFile(filename)
            }
            catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }


}