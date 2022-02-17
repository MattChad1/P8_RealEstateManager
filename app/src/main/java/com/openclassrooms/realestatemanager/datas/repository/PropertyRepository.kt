package com.openclassrooms.realestatemanager.datas.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.datas.database.PropertyDao
import com.openclassrooms.realestatemanager.datas.model.*
import com.openclassrooms.realestatemanager.utils.PhotoUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.io.*


class PropertyRepository(private val propertyDao: PropertyDao) {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val propertyCollection = db.collection("Property")
    val typeCollection = db.collection("TypeOfProperty")
    val proximityCollection = db.collection("Proximity")
    val imagesCollection = db.collection("ImageRoom")
    val agentCollection = db.collection("Agent")
    val crossRefCollection = db.collection("PropertyProximityCrossRef")
    val crossRefInfosUpdateCollection = db.collection("CrossRefInfosUpdate")


    val allPropertiesComplete: Flow<List<PropertyWithProximity>?> = propertyDao.getPropertiesComplete()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(property: Property): Int {
        return propertyDao.insert(property).toInt()
    }

    suspend fun updateProperty(property: Property) = propertyDao.update(property)

    suspend fun getPropertyById(id: Int): PropertyWithProximity = propertyDao.getPropertyCompleteById(id)

    suspend fun getPropertyCompleteById(id: Int): PropertyWithProximity = propertyDao.getPropertyCompleteById(id)

    suspend fun addPhoto(idProperty: Int, nameFile: String, legende: String) {
        propertyDao.addPhoto(ImageRoom(0, idProperty, nameFile, legende))
    }

    suspend fun deletePhoto(idProperty: Int) {
        propertyDao.deletePhoto(idProperty)
    }

    suspend fun updateProximityForProperty(idProperty: Int, proximityIds: List<Int>) {
        propertyDao.deleteProximtyForProperty(idProperty)
        for (id in proximityIds) {
            propertyDao.insertPropertyProximityCrossRef(PropertyProximityCrossRef(idProperty, id))
        }
        propertyDao.updateCrossRefInfosUpdate(CrossRefInfosUpdate(idProperty ))
    }

//    suspend fun deleteProximityForProperty(idProperty: Int) {
//        propertyDao.deleteProximtyForProperty(idProperty)
//    }
//    suspend fun insertPropertyProximityCrossRef(crossRef: PropertyProximityCrossRef) {
//        propertyDao.insertPropertyProximityCrossRef(crossRef)
//        propertyDao.updateCrossRefInfosUpdate(CrossRefInfosUpdate(crossRef.idProperty ))
//    }

    suspend fun getMaxId(): Int = propertyDao.getMaxId()

    suspend fun getAllProximities(): List<Proximity> {
        return propertyDao.getAllProximities()
    }

    val allTypes: Flow<List<TypeOfProperty>> = propertyDao.getAllTypes()

    suspend fun getAllAgents(): List<Agent> = propertyDao.getAllAgents()

    suspend fun synchroniseRoomToFirestore() {

        val agentsInRoom = propertyDao.getAllAgents()
        agentCollection.get().addOnSuccessListener() { result ->
            val agentsInFirestore = result.toObjects(Agent::class.java).toMutableList()
            for (roomItem in agentsInRoom) {
                val firestoreItem = agentsInFirestore.firstOrNull { it.idAgent == roomItem.idAgent }
                if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                    agentCollection.document(roomItem.idAgent.toString()).set(roomItem)
                    agentsInFirestore.remove(firestoreItem)
                }
            }
            runBlocking { propertyDao.insertAllAgents(agentsInFirestore) }
        }



        val proximitiesInRoom = propertyDao.getAllProximities()
        proximityCollection.get().addOnSuccessListener() { result ->
            val proximitiesInFirestore = result.toObjects(Proximity::class.java).toMutableList()
            for (roomItem in proximitiesInRoom) {
                val firestoreItem = proximitiesInFirestore.firstOrNull { it.idProximity == roomItem.idProximity }
                if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                    proximityCollection.document(roomItem.idProximity.toString()).set(roomItem)
                    proximitiesInFirestore.remove(firestoreItem)
                }
            }
            runBlocking { propertyDao.insertAllProximities(proximitiesInFirestore) }
        }

        val propertiesInRoom = propertyDao.getAllPropertiesNoFlow()
        propertyCollection.get().addOnSuccessListener() { result ->
            var propertiesInFirestore = result.toObjects(Property::class.java).toMutableList()
            if (propertiesInRoom != null) {
                for (roomItem in propertiesInRoom) {
                    val firestoreItem = propertiesInFirestore.firstOrNull { it.idProperty == roomItem.idProperty }
                    if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                        propertyCollection.document(roomItem.idProperty.toString()).set(roomItem)
                        propertiesInFirestore.remove(firestoreItem)
                    }
                }
            }
            runBlocking { propertyDao.insertAllProperties(propertiesInFirestore) }
        }


        val typesInRoom = propertyDao.getAllTypesNoFlow()
        typeCollection.get().addOnSuccessListener() {result ->
            var typesInFirestore = result.toObjects(TypeOfProperty::class.java)
        for (roomItem in typesInRoom) {
            val firestoreItem = typesInFirestore.firstOrNull { it.idType == roomItem.idType }
            if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                typeCollection.document(roomItem.idType.toString()).set(roomItem)
                typesInFirestore.remove(firestoreItem)
            }
        }
        runBlocking { propertyDao.insertAllTypes(typesInFirestore) }
    }


        val storage = Firebase.storage
        val storageRef = storage.reference
        val imagesInRoom = propertyDao.getAllImages()
        val MAX_SIZE: Long = 1024 * 1024
        imagesCollection.get().addOnSuccessListener() {result ->
            var imagesInFirestore = result.toObjects(ImageRoom::class.java)
            for (roomItem in imagesInRoom) {
                val firestoreItem = imagesInFirestore.firstOrNull { it.id == roomItem.id }
                if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                    imagesCollection.document(roomItem.id.toString()).set(roomItem)
                    val file = File(MyApplication.instance.filesDir, roomItem.nameFile +".jpg")
                    val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val imageRef: StorageReference = storageRef.child(file.name)
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        Log.i("MyLog Firebase", "upload image success : " + file.name)
                    }.addOnSuccessListener { taskSnapshot ->
                        Log.i("MyLog Firebase", "upload image failed : " + file.name)
                    }
                    imagesInFirestore.remove(firestoreItem)
                }
            }
            runBlocking {
                propertyDao.insertAllImages(imagesInFirestore)
                for (image in imagesInFirestore) {
                    var imageRef = storageRef.child(image.nameFile + ".jpg")
                    imageRef.getBytes(MAX_SIZE).addOnSuccessListener {
                        val img = BitmapFactory.decodeByteArray(it,0,it.size)
                        PhotoUtils.savePhotoToInternalStorage(image.nameFile, img)
                    }.addOnFailureListener {
                        Log.i("MyLog Firebase", "download image failed : " + image.nameFile)
                    }
                }

            }
        }

        val infosUpdateCrossRefInRoom = propertyDao.getCrossRefInfosUpdate()
        val crossRefsInRoom = propertyDao.getAllCrossRef()
        crossRefInfosUpdateCollection.get().addOnSuccessListener() {result ->
            var crossRefInfosUpdateInFirestore = result.toObjects(CrossRefInfosUpdate::class.java)
            for (roomItem in infosUpdateCrossRefInRoom) {
                val firestoreItem = crossRefInfosUpdateInFirestore.firstOrNull { it.idProperty == roomItem.idProperty }
                if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                    crossRefInfosUpdateCollection.document("${roomItem.idProperty}").set(roomItem)
                    crossRefCollection.whereEqualTo("idProperty", roomItem.idProperty).get().addOnSuccessListener {snapshot ->
                        snapshot.forEach { document->document.reference.delete() }
                        crossRefsInRoom.filter{it.idProperty==roomItem.idProperty}.forEach {
                            crossRefCollection.document("${it.idProperty}_${it.idProximity}").set(it)
                        }
                    }
                    crossRefInfosUpdateInFirestore.remove(firestoreItem)
                }
            }

            runBlocking {
                for (item in crossRefInfosUpdateInFirestore) {
                    propertyDao.updateCrossRefInfosUpdate(item)
                    crossRefCollection.whereEqualTo("idProperty", item.idProperty).get().addOnSuccessListener {
                        runBlocking {propertyDao.insertAllCrossRef(it.toObjects(PropertyProximityCrossRef::class.java))}
                    }

                }
            }
        }


//        propertyDao.getAllCrossRef().forEach { entry ->
//            val document = crossRefCollection.document("${entry.idProperty}_${entry.idProximity}")
//            document.get()
//                .addOnSuccessListener {
//                    if (it == null) {
//                        document.set((entry))
//                    } else {
//                        if (it.getLong("lastUpdate") == null || it.getLong("lastUpdate")!! < entry.lastUpdate) document.set((entry))
//                        else runBlocking<Unit> { propertyDao.insertPropertyProximityCrossRef(it.toObject(PropertyProximityCrossRef::class.java)!!) }
//
//
//                    }
//                }
//        }

        PhotoUtils.synchronisePhotosWithFirebase()

    }


}


