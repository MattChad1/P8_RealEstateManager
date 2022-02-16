package com.openclassrooms.realestatemanager.datas.repository

import androidx.annotation.WorkerThread
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.realestatemanager.datas.database.PropertyDao
import com.openclassrooms.realestatemanager.datas.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking


class PropertyRepository(private val propertyDao: PropertyDao) {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val propertyCollection = db.collection("Property")
    val typeCollection = db.collection("TypeOfProperty")
    val proximityCollection = db.collection("Proximity")
    val imagesCollection = db.collection("ImageRoom")
    val agentCollection = db.collection("Agent")
    val crossRefCollection = db.collection("PropertyProximityCrossRef")


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

    suspend fun insertPropertyProximityCrossRef(crossRef: PropertyProximityCrossRef) {
        propertyDao.insertPropertyProximityCrossRef(crossRef)
    }

    suspend fun deletePhoto(idProperty: Int) {
        propertyDao.deletePhoto(idProperty)
    }

    suspend fun deleteProximityForProperty(idProperty: Int) {
        propertyDao.deleteProximtyForProperty(idProperty)
    }

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
                    propertyCollection.document(roomItem.idAgent.toString()).set(roomItem)
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
                    propertyCollection.document(roomItem.idProximity.toString()).set(roomItem)
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



        val imagesInRoom = propertyDao.getAllImages()
        imagesCollection.get().addOnSuccessListener() {result ->
            var imagesInFirestore = result.toObjects(ImageRoom::class.java)
            for (roomItem in imagesInRoom) {
                val firestoreItem = imagesInFirestore.firstOrNull { it.id == roomItem.id }
                if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
                    imagesCollection.document(roomItem.id.toString()).set(roomItem)
                    imagesInFirestore.remove(firestoreItem)
                }
            }
            runBlocking { propertyDao.insertAllImages(imagesInFirestore) }
        }

//        val crossRefInRoom = propertyDao.getAllCrossRef()
//        crossRefCollection.get().addOnSuccessListener() {result ->
//            var crossRefInFirestore = result.toObjects(PropertyProximityCrossRef::class.java)
//            for (roomItem in imagesInRoom) {
//                val firestoreItem = crossRefInFirestore.firstOrNull { it.id == roomItem.id }
//                if (firestoreItem == null || (firestoreItem.lastUpdate < roomItem.lastUpdate)) {
//                    imagesCollection.document("${roomItem.idProperty}_${roomItem.idProximity}").set(roomItem)
//                    imagesInFirestore.remove(firestoreItem)
//                }
//            }
//            runBlocking { propertyDao.insertAllImages(imagesInFirestore) }
//        }


        propertyDao.getAllCrossRef().forEach { entry ->
            val document = crossRefCollection.document("${entry.idProperty}_${entry.idProximity}")
            document.get()
                .addOnSuccessListener {
                    if (it == null) {
                        document.set((entry))
                    } else {
                        if (it.getLong("lastUpdate") == null || it.getLong("lastUpdate")!! < entry.lastUpdate) document.set((entry))
                        else runBlocking<Unit> { propertyDao.insertPropertyProximityCrossRef(it.toObject(PropertyProximityCrossRef::class.java)!!) }


                    }
                }
        }

    }


}


