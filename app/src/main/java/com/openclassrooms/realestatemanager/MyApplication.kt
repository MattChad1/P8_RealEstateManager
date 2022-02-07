package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.datas.database.LocaleDatabase
import com.openclassrooms.realestatemanager.datas.repository.FilterSearchRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var propertyRepository: PropertyRepository
    lateinit var filterSearchRepository: FilterSearchRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        val database by lazy { LocaleDatabase.getInstance(this, applicationScope) }
        propertyRepository = PropertyRepository(database.propertyDao(), database.imageRoomDao(), database.typeOfPropertyDao())
        filterSearchRepository = FilterSearchRepository()

    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }


}