package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.datas.database.LocaleDatabase
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var propertyRepository: DefaultPropertyRepository
    lateinit var navigationRepository: NavigationRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        val database by lazy { LocaleDatabase.getInstance(this, applicationScope) }
        propertyRepository = DefaultPropertyRepository(database.propertyDao())
        navigationRepository = NavigationRepository()
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }


}