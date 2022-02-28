package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.datas.database.LocaleDatabase
import com.openclassrooms.realestatemanager.datas.repository.NavigationRepository
import com.openclassrooms.realestatemanager.datas.repository.DefaultPropertyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var propertyRepository: DefaultPropertyRepository
    lateinit var navigationRepository: NavigationRepository
    open var isTestMode = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (!isTestMode) {
            val database by lazy { LocaleDatabase.getInstance(this, applicationScope) }
            propertyRepository = DefaultPropertyRepository(database.propertyDao())
            navigationRepository = NavigationRepository()
        }

    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }


}