package com.openclassrooms.realestatemanager.datas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.datas.model.*
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
                    populateDatabase(database.propertyDao(), database.agentDao(), database.typeOfPropertyDao(), database.proximityDao(), database.propertyProximityCrossRefDao())
                }
            }
        }

        suspend fun populateDatabase(propertyDao: PropertyDao, agentDao: AgentDao, typeOfPropertyDao: TypeOfPropertyDao, proximityDao: ProximityDao, propertyProximityCrossRefDao: PropertyProximityCrossRefDao) {
            agentDao.insert(Agent(0, "Mike Money"))
            agentDao.insert(Agent(0, "Melissa BigDollars"))


            typeOfPropertyDao.insert(TypeOfProperty(1, "Condo"))
            typeOfPropertyDao.insert(TypeOfProperty(2, "Loft"))
            typeOfPropertyDao.insert(TypeOfProperty(3, "Mansion"))
            typeOfPropertyDao.insert(TypeOfProperty(4, "Single Family House"))


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
                    mutableListOf<ImageRoom>(ImageRoom(0, 0, "flat1", "Salon"),
                        ImageRoom(0, 0, "flat2", "Autre pièce")),

//                    mutableListOf<ProximityEnum>(ProximityEnum.PARK, ProximityEnum.SCHOOL),
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
                    mutableListOf<ImageRoom>(ImageRoom(0, 0, "flat2", "Autre pièce")),

//                    mutableListOf<ProximityEnum>(ProximityEnum.PARK, ProximityEnum.PUBLIC_TRANSPORTS),
                    "2022-01-01",
                    null
                )
            )

            proximityDao.insertAll(
                listOf(
                    Proximity(1, "School"),
                    Proximity(2, "Stores"),
                    Proximity(3, "Public transports"),
                    Proximity(4, "Park")
                )
            )

            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(1,2))
            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(1,3))
            propertyProximityCrossRefDao.insert(PropertyProximityCrossRef(2,2))



        }
    }


}