package com.openclassrooms.realestatemanager.datas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.datas.model.Converters
import com.openclassrooms.realestatemanager.datas.model.Property

@Database(entities = [Property::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class LocaleDatabase: RoomDatabase() {

    abstract val propertyDao: PropertyDao

    companion object {
        @Volatile
        private var INSTANCE: LocaleDatabase? = null



        fun getInstance(context: Context): LocaleDatabase {

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocaleDatabase::class.java,
                        "real_estate_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance

                }

                return instance!!
            }


        }

    }


}