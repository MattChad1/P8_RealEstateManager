package com.openclassrooms.realestatemanager.datas.database

import android.content.Context
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.datas.model.*
import com.openclassrooms.realestatemanager.utils.PhotoUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Property::class, Agent::class, TypeOfProperty::class, Proximity::class, PropertyProximityCrossRef::class, ImageRoom::class, CrossRefInfosUpdate::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(Converters::class)
abstract class LocaleDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

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

                return instance
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
                        database.propertyDao()
                    )
                }
            }
        }

        suspend fun populateDatabase(
            propertyDao: PropertyDao
        ) {
            propertyDao.insertAllAgents(PrepopulateDatas.preAgents)
            propertyDao.insertAllTypes(PrepopulateDatas.preTypes)
            propertyDao.insertAllProximities(PrepopulateDatas.preProximities)
            propertyDao.insertAllCrossRef(PrepopulateDatas.preCrossRef)
            propertyDao.insertAllImages(PrepopulateDatas.prePhotos)
            propertyDao.insertAllProperties(PrepopulateDatas.preProperties)

            PhotoUtils.savePhotoToInternalStorage("flat1", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat1))
            PhotoUtils.savePhotoToInternalStorage("flat2", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat2))
            PhotoUtils.savePhotoToInternalStorage("flat3", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat3))
            PhotoUtils.savePhotoToInternalStorage("flat4", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat4))
            PhotoUtils.savePhotoToInternalStorage("flat5", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat5))
            PhotoUtils.savePhotoToInternalStorage("flat6", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat6))
            PhotoUtils.savePhotoToInternalStorage("flat7", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat7))
            PhotoUtils.savePhotoToInternalStorage("flat8", BitmapFactory.decodeResource(MyApplication.instance.resources, R.drawable.flat8))


        }
    }


}