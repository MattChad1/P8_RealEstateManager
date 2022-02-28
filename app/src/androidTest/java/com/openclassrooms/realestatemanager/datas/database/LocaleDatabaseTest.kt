package com.openclassrooms.realestatemanager.datas.database

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LocaleDatabaseTest {


    private lateinit var propertyDao: PropertyDao
    private lateinit var db: LocaleDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, LocaleDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        propertyDao = db.propertyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

//    @Test
//    @Throws(Exception::class)
//    fun insertAndGet() {
//        val property = Property(
//            0,
//            4,
//            "1075 Victory Blvd, Staten Island, NY 10314",
//            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis eleifend tortor. Phasellus consequat velit risus, sed tincidunt massa gravida eu. Etiam justo eros, ultrices ac efficitur vel, efficitur nec sem. Duis pharetra vulputate hendrerit. Fusce pellentesque, nunc quis lacinia pellentesque, odio ante iaculis nulla, non consequat ante ipsum a justo. Nam a elit sed ipsum feugiat eleifend. Sed non.",
//            2,
//            3500000,
//            150.00,
//            2,
//            1,
//            1,
//            "2022-01-01",
//            "2022-02-01"
//        )
//        val id = propertyDao.insert(property)
//        val propertyGet = propertyDao.getPropertyById(id)
//        assertEquals(property.agent, propertyGet.agent)
//        assertEquals(property.type, propertyGet.type)
//    }


}