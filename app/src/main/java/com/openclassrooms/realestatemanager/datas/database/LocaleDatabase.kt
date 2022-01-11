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
import com.openclassrooms.realestatemanager.datas.model.Property
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Property::class, Agent::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class LocaleDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun agentDao(): AgentDao

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
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        null,
                        "8 5th Avenue",
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
                        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        null,
                        "150 5th Avenue",
                        mutableListOf<ProximityEnum>(ProximityEnum.PARK, ProximityEnum.PUBLIC_TRANSPORTS),
                        true,
                        "2022-01-01",
                        null
                    )
                )

            }
        }


}