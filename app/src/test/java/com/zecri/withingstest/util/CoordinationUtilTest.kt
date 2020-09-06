package com.zecri.withingstest.util

import android.content.res.Resources
import com.zecri.withingstest.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CoordinationUtilTest {

    @MockK
    lateinit var resources: Resources

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)


//    @Nested
//    @DisplayName("error message")
//    inner class ErrorMessage {

    @Test
    fun `When null throwable or null throwable message, then return default error message`() {
        var throwable: Throwable? = null
        val expected = "Something went wrong"
        every { resources.getString(R.string.error) } returns (expected)

        Assert.assertEquals(expected, resources.getErrorMessage(throwable))

        throwable = IndexOutOfBoundsException()
        Assert.assertEquals(expected, resources.getErrorMessage(throwable))
    }

    @Test
    fun `When illegal argument exception, then return invalid input message`() {
        val throwable: Throwable? = IllegalArgumentException("unsupported lang")
        val expected = "Invalid input"
        every { resources.getString(R.string.invalid_input) } returns expected

        Assert.assertEquals(expected, resources.getErrorMessage(throwable))
    }

    @Test
    fun `When empty response exception, then return no result for parameters message`() {
        val throwable = EmptyResponseException("husky samoyed")
        val expected = "No result for husky+samoyed"
        every { resources.getString(R.string.empty_hits) } returns (expected)

        Assert.assertEquals(expected, resources.getErrorMessage(throwable))
    }

    @Test
    fun `When no connectivity exception, then return no internet message`() {
        val throwable = NoConnectivityException()
        val expected = "No internet connection"
        every { resources.getString(R.string.offline) } returns (expected)

        Assert.assertEquals(expected, resources.getErrorMessage(throwable))
    }

    @Test
    fun `When non null exception that is not NoConnectivityException with non null message, then return exception message`() {
        val throwable = Exception("exception message")
        val expected = "exception message"
        Assert.assertEquals(expected, resources.getErrorMessage(throwable))
    }

    // }

    @After
    fun tearDown() {
        unmockkAll()
    }
}