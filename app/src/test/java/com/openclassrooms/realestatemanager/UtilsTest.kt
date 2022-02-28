package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SdkSuppress
import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

import java.text.DecimalFormat
import java.util.*

@RunWith(RobolectricTestRunner::class)
class UtilsTest {

//    private var connectivityManager: ConnectivityManager? = null
//    private var shadowOfActiveNetworkInfo: ShadowNetworkInfo? = null

    lateinit var connectivityManager: ConnectivityManager

@Mock
lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        connectivityManager = findConnectivityManager()
    }


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


    @Test
//    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.M)
    @Config(sdk = [29])
    fun `Checking internet connection above Android M with success`() {
        val shadowNetworkCapabilities =
            Shadows.shadowOf(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork))
        //Has Internet
        shadowNetworkCapabilities.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        Assert.assertTrue(Utils.isInternetAvailable(context))

        //Hasn't Internet
        shadowNetworkCapabilities.removeTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        shadowNetworkCapabilities.removeTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowNetworkCapabilities.addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        Assert.assertFalse(Utils.isInternetAvailable(context))

    }




    @Test
//    @SdkSuppress(maxSdkVersion = 22)
    @Config(sdk = [22])
    fun getActiveNetworkInfo_shouldReturnTrueCorrectlyOldVersion() {
        val networkInfo: NetworkInfo = mock(NetworkInfo::class.java)
        `when`(networkInfo.isConnected).thenReturn(true)
        `when`(networkInfo.isConnectedOrConnecting).thenReturn(true)

        val capabilities: NetworkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(true)
        val connectivityManager: ConnectivityManager = mock(ConnectivityManager::class.java)
        `when`(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)).thenReturn(capabilities)


        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)

        assertTrue(Utils.isInternetAvailable(context))
    }

    private fun findConnectivityManager() =
        ApplicationProvider.getApplicationContext<Context>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager


    }



