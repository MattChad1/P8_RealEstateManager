package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.datas.database.LocaleDatabase
import com.openclassrooms.realestatemanager.datas.database.PropertyDao
import com.openclassrooms.realestatemanager.datas.repository.PropertyRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

lateinit var propertyRepository: PropertyRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        val database by lazy { LocaleDatabase.getInstance(this, applicationScope) }
        propertyRepository = PropertyRepository(database.propertyDao())

    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }


}