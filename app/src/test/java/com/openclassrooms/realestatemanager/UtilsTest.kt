package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Test
import java.text.DecimalFormat
import java.util.*

class UtilsTest {
    @Test
    fun convertDollarToEuro() {
        val entry: Long = 10
        val expected: Long = 8
        Assert.assertEquals(expected, Utils.convertDollarToEuro(entry))
    }

    @Test
    fun convertEuroToDollar() {
        val entry: Long = 10
        val expected: Long = 12
        Assert.assertEquals(expected, Utils.convertEuroToDollar(entry))
    }

    @Test
    fun getTodayDate() {
        val dateResult = Utils.getTodayDate()
        assertTrue(dateResult!!.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
        val now = Calendar.getInstance()
        val mFormat = DecimalFormat("00")
        assertEquals(mFormat.format(now[Calendar.DAY_OF_MONTH].toLong()) + "/" + mFormat.format((now[Calendar.MONTH] + 1).toLong()) + "/" + now[Calendar.YEAR], dateResult)
    }

    @Test
    fun formatPrice() {
        val tested: Long = 1000000
        val expected = "$1,000,000"
        assertEquals(expected, Utils.formatPrice(tested))

        val tested2: Long = 999
        val expected2 = "$999"
        assertEquals(expected2, Utils.formatPrice(tested2))

    }
}