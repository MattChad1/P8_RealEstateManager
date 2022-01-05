package com.openclassrooms.realestatemanager

import org.junit.runner.RunWith
import android.support.test.runner.AndroidJUnit4
import kotlin.Throws
import android.support.test.InstrumentationRegistry
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.openclassrooms.realestatemanager.R
import org.junit.Assert
import org.junit.Test
import java.lang.Exception

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("com.openclassrooms.go4lunch", appContext.packageName)
    }
}