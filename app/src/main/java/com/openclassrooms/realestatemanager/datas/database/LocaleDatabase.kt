package com.openclassrooms.realestatemanager.datas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.datas.enumClass.ProximityEnum
import com.openclassrooms.realestatemanager.datas.enumClass.TypeEnum
import com.openclassrooms.realestatemanager.datas.model.Agent
import com.openclassrooms.realestatemanager.datas.model.Converters
import com.openclassrooms.realestatemanager.datas.model.ImageRoom
import com.openclassrooms.realestatemanager.datas.model.Property
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Property::class, Agent::class/*, ImageRoom::class*/], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class LocaleDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun agentDao(): AgentDao
//    abstract fun imageRoomDao(): ImageRoomDao

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
                        populateDatabase(database.propertyDao(), database.agentDao())
                    }
                }
            }

            suspend fun populateDatabase(propertyDao: PropertyDao, agentDao: AgentDao) {
                agentDao.insert(Agent(0, "Mike Money"))
                agentDao.insert(Agent(0, "Melissa BigDollars"))


                propertyDao.insert(
                    Property(
                        0,
                        TypeEnum.LOFT,
                        1,
                        1500000,
                        300.00,
                        6,
                        4,
                        2,
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                        mutableListOf<ImageRoom>(ImageRoom(0 , 0, "flat1", "Salon"), ImageRoom(0 , 0, "flat2", "Autre pièce")),
                        "386 Woolley Ave, Staten Island, NY 10314",
                        mutableListOf<ProximityEnum>(ProximityEnum.PARK, ProximityEnum.SCHOOL),
                        true,
                        "2022-01-01",
                        null
                    )
                )

                propertyDao.insert(
                    Property(
                        0,
                        TypeEnum.LOFT,
                        1,
                        999000,
                        100.00,
                        4,
                        3,
                        1,
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
                        mutableListOf<ImageRoom>(ImageRoom(0 , 0, "flat2", "Autre pièce")),
                        "2071 Victory Blvd, Staten Island, NY 10314",
                        mutableListOf<ProximityEnum>(ProximityEnum.PARK, ProximityEnum.PUBLIC_TRANSPORTS),
                        true,
                        "2022-01-01",
                        null
                    )
                )

            }
        }


}