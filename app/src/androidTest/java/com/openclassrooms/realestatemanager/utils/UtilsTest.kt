package com.openclassrooms.realestatemanager.utils

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.MyApplication
import com.openclassrooms.realestatemanager.ui.main_activity.MainActivity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UtilsTest {

    @Test
    fun checkAppWillWork_withNoInternetConnection() {
        launchActivity<MainActivity>().use {

            assertTrue(Utils.isInternetAvailable(MyApplication.instance))
            InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable");
            InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable");
            Thread.sleep(3000);
            assertFalse(Utils.isInternetAvailable(MyApplication.instance))
            InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
            InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");

        }
    }

}


