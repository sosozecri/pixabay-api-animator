package com.zecri.withingstest.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.HttpException

@ExtendWith(MockKExtension::class)

internal class NetworkUtilTest {

    //    @Nested
//    @DisplayName("network exception")
//    inner class NetworkException {

    @MockK
    lateinit var httpException: HttpException

    @Before
    fun setUp() =
        MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun `When code equal to 400, then return true`() {
        every { httpException.code() } returns 400
        assertTrue(httpException.isBadRequest())
    }

    @Test
    fun `When code not equal to 400, then return false`() {
        every { httpException.code() } returns 429
        assertFalse(httpException.isBadRequest())
    }

    //}

    //    @Nested
//    @DisplayName("connectivity")
//    inner class Connectivity {


    @MockK
    lateinit var context: Context

    @MockK
    lateinit var connectivityManager: ConnectivityManager

    @MockK
    lateinit var networkInfo: NetworkInfo

    @MockK
    lateinit var networkCapabilities: NetworkCapabilities

    @MockK
    lateinit var activeNetwork: Network

    @Test
    fun `When offline, then return false`() {
        every { connectivityManager.activeNetworkInfo } returns networkInfo
        every { networkInfo.isConnectedOrConnecting } returns false
        assertFalse(connectivityManager.isOnlineLegacy())

        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        assertFalse(connectivityManager.isOnlineLegacy())

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.isOnline() } returns false
        every { connectivityManager.isOnlineLegacy() } returns false
        assertFalse(context.isOnline())
    }

    @Test
    fun `When online, then return true`() {
        every { connectivityManager.activeNetworkInfo } returns networkInfo
        every { networkInfo.isConnectedOrConnecting } returns true
        assertTrue(connectivityManager.isOnlineLegacy())

        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        assertTrue(connectivityManager.isOnlineLegacy())

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.isOnline() } returns true
        every { connectivityManager.isOnlineLegacy() } returns true
        assertTrue(context.isOnline())
    }
    //}

    @After
    fun tearDown() {
        unmockkAll()
    }
}
