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
import java.text.DecimalFormat
import java.util.*

class UtilsTest {
    @Test
    fun convertDollarToEuro() {
        val entry = 10
        val expected = 8
        Assert.assertEquals(expected.toLong(), Utils.convertDollarToEuro(entry).toLong())
    }

    @Test
    fun convertEuroToDollar() {
        val entry = 10
        val expected = 12
    }

    @Test
    fun getTodayDate() {
        val dateResult = Utils.getTodayDate()
        Assert.assertTrue(dateResult.matches("\\d{2}/\\d{2}/\\d{4}"))
        val now = Calendar.getInstance()
        val mFormat = DecimalFormat("00")
        Assert.assertEquals(mFormat.format(now[Calendar.DAY_OF_MONTH].toLong()) + "/" + mFormat.format((now[Calendar.MONTH] + 1).toLong()) + "/" + now[Calendar.YEAR], dateResult)
    }
}