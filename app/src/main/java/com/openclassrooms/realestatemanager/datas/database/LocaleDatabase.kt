package com.openclassrooms.realestatemanager.datas.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.datas.model.*
import com.openclassrooms.realestatemanager.utils.PhotoUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Property::class, Agent::class, TypeOfProperty::class, Proximity::class, PropertyProximityCrossRef::class, ImageRoom::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocaleDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun agentDao(): AgentDao
    abstract fun typeOfPropertyDao(): TypeOfPropertyDao
    abstract fun proximityDao(): ProximityDao
    abstract fun propertyProximityCrossRefDao(): PropertyProximityCrossRefDao
    abstract fun imageRoomDao(): ImageRoomDao

    companion object {
        @Volatile
        private var INSTANCE: LocaleDatabase? = null


        fun getInstance(
            context: Context, scope: CoroutineScope
        ): LocaleDatabase {

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocaleDatabase::class.java,
                        "real_estate_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(LocaleDatabaseCallback(scope))
                        .build()
                    INSTANCE = instance

                }

                return instance!!
            }


        }
    }


    private class LocaleDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.propertyDao(),
                        database.agentDao(),
                        database.typeOfPropertyDao(),
                        database.proximityDao(),
                        database.propertyProximityCrossRefDao(),
                        database.imageRoomDao()
                    )
                }
            }
        }

        suspend fun populateDatabase(
            propertyDao: PropertyDao,
            agentDao: AgentDao,
            typeOfPropertyDao: TypeOfPropertyDao,
            proximityDao: ProximityDao,
            propertyProximityCrossRefDao: PropertyProximityCrossRefDao,
            imageRoomDao: ImageRoomDao
        ) {
            agentDao.insert(Agent(0, "Mike Money"))
            agentDao.insert(Agent(0, "Melissa BigDollars"))


            typeOfPropertyDao.insert(TypeOfProperty(1, "Condo"))
            typeOfPropertyDao.insert(TypeOfProperty(2, "Loft"))
            typeOfPropertyDao.insert(TypeOfProperty(3, "Mansion"))
            typeOfPropertyDao.insert(TypeOfProperty(4, "Single Family House"))

            imageRoomDao.insert(ImageRoom(0, 1, "flat1", "Living-room"))
            imageRoomDao.insert(ImageRoom(0, 2, "flat2", "A room"))
            imageRoomDao.insert(ImageRoom(0, 1, "flat2", "Another room"))
            imageRoomDao.insert(ImageRoom(0, 3, "flat3", "Living-room"))


            propertyDao.insert(
                Property(
                    0,
                    1,
                    "386 Woolley Ave, Staten Island, NY 10314",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                    1,
                    1500000,
                    300.00,
                    6,
                    4,
                    2,
                    "2022-01-01",
                    null
                )
            )

            propertyDao.insert(
                Property(
                    0,
                    2,
                    "2071 Victory Blvd, Staten Island, NY 10314",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                    1,
                    999000,
                    100.00,
                    4,
                    3,
                    1,
                    "2022-01-01",
                    null
                )
            )

            propertyDao.insert(
                Property(
                    0,
                    2,
                    "2075 Victory Blvd, Staten Island, NY 10314",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                    1,
                    100000,
                    30.00,
                    2,
                    1,
                    1,
                    "2022-01-01",
                    null
                )
            )

            proximityDao.insertAll(
                listOf(
                    Proximity(1, "School", "icon_proximity_school", "proximity_school"),
                    Proximity(2, "Stores", "icon_proximity_stores", "proximity_stores"),
                    Proximity(3, "Public transports", "icon_proximity_transports", "proximity_transports"),
                    Proximity(4, "Park", "icon_proximity_park", "proximity_park")
                )
            )

            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(1, 1))
            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(1, 2))
            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(1, 3))
            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(1, 4))
            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(2, 1))
            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(2, 3))

            PhotoUtils.savePhotoToInternalStorage("flat1", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat1))
            PhotoUtils.savePhotoToInternalStorage("flat2", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat2))
            PhotoUtils.savePhotoToInternalStorage("flat3", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat3))


        }
    }


}