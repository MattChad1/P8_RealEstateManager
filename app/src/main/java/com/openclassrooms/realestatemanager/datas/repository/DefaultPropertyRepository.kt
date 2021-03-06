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
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


class DefaultPropertyRepository(private val propertyDao: PropertyDao) : PropertyRepository {

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val propertyCollection = db.collection("Property")
    private val typeCollection = db.collection("TypeOfProperty")
    private val proximityCollection = db.collection("Proximity")
    private val imagesCollection = db.collection("ImageRoom")
    private val agentCollection = db.collection("Agent")
    private val crossRefCollection = db.collection("PropertyProximityCrossRef")
    private val crossRefInfosUpdateCollection = db.collection("CrossRefInfosUpdate")


    override fun getAllPropertiesComplete(): Flow<List<PropertyWithProximity>?> = propertyDao.getPropertiesComplete()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(property: Property): Int {
        return propertyDao.insert(property).toInt()
    }

    override suspend fun updateProperty(property: Property) = propertyDao.update(property)

    override suspend fun getPropertyCompleteById(id: Int): PropertyWithProximity = propertyDao.getPropertyCompleteById(id)

    override suspend fun addPhoto(idProperty: Int, nameFile: String, legende: String, idBase: Int) {
        propertyDao.addPhoto(ImageRoom(idBase, idProperty, nameFile, legende))
    }

    override suspend fun deletePhoto(idProperty: Int) {
        propertyDao.deletePhoto(idProperty)
    }

    override suspend fun updateProximityForProperty(idProperty: Int, proximityIds: List<Int>) {
        propertyDao.deleteProximtyForProperty(idProperty)
        for (id in proximityIds) {
            propertyDao.insertPropertyProximityCrossRef(PropertyProximityCrossRef(idProperty, id))
        }
        propertyDao.updateCrossRefInfosUpdate(CrossRefInfosUpdate(idProperty))
    }

    override suspend fun getMaxId(): Int = propertyDao.getMaxId()

    override suspend fun getAllProximities(): List<Proximity> {
        return propertyDao.getAllProximities()
    }

    override fun getAllTypes(): Flow<List<TypeOfProperty>> {
        return propertyDao.getAllTypes()
    }

    override suspend fun getAllAgents(): List<Agent> = propertyDao.getAllAgents()


    override suspend fun synchroniseRoomToFirestore() {

        val agentsInRoom = propertyDao.getAllAgents()
        val resultFirebaseAgents = agentCollection.get().await()
        val agentsInFirestore = resultFirebaseAgents.toObjects(Agent::class.java).toMutableList()
        for (roomItem in agentsInRoom) {
            val firestoreItem = agentsInFirestore.firstOrNull { it.idAgent == roomItem.idAgent }
            if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                agentCollection.document(roomItem.idAgent.toString()).set(roomItem).await()
                agentsInFirestore.remove(firestoreItem)
            }
        }
        propertyDao.insertAllAgents(agentsInFirestore)


        val proximitiesInRoom = propertyDao.getAllProximities()
        val resultFirebaseProximities = proximityCollection.get().await()
        val proximitiesInFirestore = resultFirebaseProximities.toObjects(Proximity::class.java).toMutableList()
        for (roomItem in proximitiesInRoom) {
            val firestoreItem = proximitiesInFirestore.firstOrNull { it.idProximity == roomItem.idProximity }
            if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                proximityCollection.document(roomItem.idProximity.toString()).set(roomItem).await()
                proximitiesInFirestore.remove(firestoreItem)
            }
        }
        propertyDao.insertAllProximities(proximitiesInFirestore)

        val propertiesInRoom = propertyDao.getAllPropertiesNoFlow()
        val resultFirebaseProperties = propertyCollection.get().await()
        val propertiesInFirestore = resultFirebaseProperties.toObjects(Property::class.java).toMutableList()
        if (propertiesInRoom != null) {
            for (roomItem in propertiesInRoom) {
                val firestoreItem = propertiesInFirestore.firstOrNull { it.idProperty == roomItem.idProperty }
                if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                    propertyCollection.document(roomItem.idProperty.toString()).set(roomItem).await()
                    propertiesInFirestore.remove(firestoreItem)
                }
            }
        }
        propertyDao.insertAllProperties(propertiesInFirestore)


        val typesInRoom = propertyDao.getAllTypesNoFlow()
        val resultFirebaseTypes = typeCollection.get().await()
        val typesInFirestore = resultFirebaseTypes.toObjects(TypeOfProperty::class.java)
        for (roomItem in typesInRoom) {
            val firestoreItem = typesInFirestore.firstOrNull { it.idType == roomItem.idType }
            if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                typeCollection.document(roomItem.idType.toString()).set(roomItem).await()
                typesInFirestore.remove(firestoreItem)
            }
        }
        propertyDao.insertAllTypes(typesInFirestore)


        val storage = Firebase.storage
        val storageRef = storage.reference
        val maxSize: Long = 1024 * 1024


        val imagesInRoom = propertyDao.getAllImages()
        val resultFirebaseImageRoom = imagesCollection.get().await()
        val imagesInFirestore = resultFirebaseImageRoom.toObjects(ImageRoom::class.java)
        for (roomItem in imagesInRoom) {
            val firestoreItem = imagesInFirestore.firstOrNull { it.id == roomItem.id }
            if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                imagesCollection.document(roomItem.id.toString()).set(roomItem).await()
                imagesInFirestore.remove(firestoreItem)
                val file = File(MyApplication.instance.filesDir, roomItem.nameFile + ".jpg")
                val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val imageRef: StorageReference = storageRef.child(file.name)
                imageRef.putBytes(data).await()
            } else if (firestoreItem.lastUpdate == roomItem.lastUpdate) imagesInFirestore.remove(firestoreItem)
        }

        propertyDao.insertAllImages(imagesInFirestore)
        for (image in imagesInFirestore) {
            val imageRef = storageRef.child(image.nameFile + ".jpg")
            imageRef.getBytes(maxSize).addOnSuccessListener {
                val img = BitmapFactory.decodeByteArray(it, 0, it.size)
                PhotoUtils.savePhotoToInternalStorage(image.nameFile, img)
            }.addOnFailureListener {
                Log.i("MyLog Firebase", "download image failed : " + image.nameFile)
            }
        }


        val infosUpdateCrossRefInRoom = propertyDao.getCrossRefInfosUpdate()
        val crossRefsInRoom = propertyDao.getAllCrossRef()
        val resultFirebaseInfosUpdateCrossRef = crossRefInfosUpdateCollection.get().await()
        val crossRefInfosUpdateInFirestore = resultFirebaseInfosUpdateCrossRef.toObjects(CrossRefInfosUpdate::class.java)
        for (roomItem in infosUpdateCrossRefInRoom) {
            val firestoreItem = crossRefInfosUpdateInFirestore.firstOrNull { it.idProperty == roomItem.idProperty }
            if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                crossRefInfosUpdateCollection.document("${roomItem.idProperty}").set(roomItem)
                val resultFirebaseCrossRefItems = crossRefCollection.whereEqualTo("idProperty", roomItem.idProperty).get().await()
                resultFirebaseCrossRefItems.forEach { it.reference.delete().await() }
                crossRefsInRoom.filter { it.idProperty == roomItem.idProperty }.forEach {
                    crossRefCollection.document("${it.idProperty}_${it.idProximity}").set(it)
                }
            }
            crossRefInfosUpdateInFirestore.remove(firestoreItem)
        }


        for (item in crossRefInfosUpdateInFirestore) {
            propertyDao.updateCrossRefInfosUpdate(item)
            val itemFirebase = crossRefCollection.whereEqualTo("idProperty", item.idProperty).get().await()
            propertyDao.insertAllCrossRef(itemFirebase.toObjects(PropertyProximityCrossRef::class.java))
        }


        PhotoUtils.synchronisePhotosWithFirebase()
    }

}


