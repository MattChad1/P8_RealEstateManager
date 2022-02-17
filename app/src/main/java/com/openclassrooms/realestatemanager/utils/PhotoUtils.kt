package com.openclassrooms.realestatemanager.utils

import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.datas.model.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.security.auth.login.LoginException


class PhotoUtils {

    companion object {
        fun savePhotoToInternalStorage(filename: String, bmp: Bitmap): Boolean {
            return try {
                MyApplication.instance.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
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


    fun synchronisePhotosWithFirebase() {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val allFiles = MyApplication.instance.filesDir.listFiles { directory, filename -> filename.endsWith(".jpg") }
        for (file in allFiles) {
            val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val imageRef: StorageReference = storageRef.child(file.name)
            val uploadTask = imageRef.putBytes(data)

            uploadTask.addOnFailureListener {
                Log.i("MyLog Firebase", "upload success")
            }.addOnSuccessListener { taskSnapshot ->
                Log.i("MyLog Firebase", "upload failed")
            }

        }


    }

    }
}