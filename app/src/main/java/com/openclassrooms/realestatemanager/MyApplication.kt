package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.datas.database.LocaleDatabase
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var propertyRepository: PropertyRepository
    lateinit var navigationRepository: NavigationRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        val database by lazy { LocaleDatabase.getInstance(this, applicationScope) }
        propertyRepository = PropertyRepository(database.propertyDao(), database.imageRoomDao(), database.typeOfPropertyDao())
        navigationRepository = NavigationRepository()

    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }


}