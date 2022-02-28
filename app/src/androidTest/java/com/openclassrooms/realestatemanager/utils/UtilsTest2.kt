package com.openclassrooms.realestatemanager.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UtilsTest2 {

        var appContext: Context? = null
        private val device = UiDevice.getInstance(getInstrumentation())
        @Before
        fun useAppContext() {
            // Context of the app under test.
            appContext = ApplicationProvider.getApplicationContext()
        }

        @Test
        @Throws(IOException::class, InterruptedException::class)
        fun checkWithNetworkOff() {
            device.executeShellCommand("svc wifi disable")
            device.executeShellCommand("svc data disable")
            Thread.sleep(3000)
            assertFalse(Utils.isInternetAvailable(appContext!!))
        }

        @Test
        @Throws(IOException::class, InterruptedException::class)
        fun checkIsConnected() {
            device.executeShellCommand("svc wifi enable")
            device.executeShellCommand("svc data enable")
            Thread.sleep(2000)
            assertTrue(Utils.isInternetAvailable(appContext!!))
        }

        @After
        @Throws(IOException::class)
        fun enableNetwork() {
            device.executeShellCommand("svc wifi enable")
            device.executeShellCommand("svc data enable")
        }
    }



}