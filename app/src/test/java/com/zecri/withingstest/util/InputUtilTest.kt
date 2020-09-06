package com.zecri.withingstest.util

import android.os.Bundle
import com.zecri.withingstest.data.source.remote.LANG
import com.zecri.withingstest.data.source.remote.PAGE
import com.zecri.withingstest.data.source.remote.PER_PAGE
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class InputUtilTest {

    @MockK
    lateinit var bundle: Bundle

    @Before
    fun setUp() =
        MockKAnnotations.init(this, relaxUnitFun = true)

    //    @Nested
//    @DisplayName("invalid input")
//    inner class InvalidInput {
    @Test
    fun `When bundle size exceeds the maximum, then throw illegal argument exception `() {
        every { bundle.size() } returns 3

        assertThrows<IllegalArgumentException> { throwExceptionWhenInvalid(bundle, 2) }
    }

    @Test
    fun `When bundle size is equal to the maximum, then no exception raised`() {
        every { bundle.size() } returns 2
        every { bundle.containsKey(LANG) } returns false
        every { bundle.containsKey(PAGE) } returns false
        every { bundle.containsKey(PER_PAGE) } returns false

        assertDoesNotThrow { throwExceptionWhenInvalid(bundle, 2) }
    }

    @Test
    fun `When bundle size is lower than the maximum, then no exception raised`() {
        every { bundle.size() } returns 1
        every { bundle.containsKey(LANG) } returns false
        every { bundle.containsKey(PAGE) } returns false
        every { bundle.containsKey(PER_PAGE) } returns false

        assertDoesNotThrow { throwExceptionWhenInvalid(bundle, 2) }
    }

    @Test
    fun `When lang unknown, then illegal argument exception raised`() {
        every { bundle.size() } returns 2
        every { bundle.containsKey(LANG) } returns true
        every { bundle.getString(LANG) } returns "lang"

        assertThrows<IllegalArgumentException> { throwExceptionWhenInvalid(bundle, 2) }
    }

    @Test
    fun `When page lower than 1, then illegal argument exception raised`() {
        every { bundle.size() } returns 2
        every { bundle.containsKey(LANG) } returns true
        every { bundle.getString(LANG) } returns "en"
        every { bundle.containsKey(PAGE) } returns true
        every { bundle.getInt(PAGE) } returns 0

        assertThrows<IllegalArgumentException> { throwExceptionWhenInvalid(bundle, 2) }
    }

    @Test
    fun `When page higher than 1, then no exception raised`() {
        every { bundle.size() } returns 2
        every { bundle.containsKey(LANG) } returns true
        every { bundle.getString(LANG) } returns "en"
        every { bundle.containsKey(PAGE) } returns true
        every { bundle.getInt(PAGE) } returns 2
        every { bundle.containsKey(PER_PAGE) } returns false

        assertDoesNotThrow { throwExceptionWhenInvalid(bundle, 2) }
    }

    @Test
    fun `When per page lower than 3, then illegal argument exception raised`() {
        every { bundle.size() } returns 2
        every { bundle.containsKey(LANG) } returns true
        every { bundle.getString(LANG) } returns "en"
        every { bundle.containsKey(PAGE) } returns true
        every { bundle.getInt(PAGE) } returns 0
        every { bundle.containsKey(PER_PAGE) } returns true
        every { bundle.getInt(PER_PAGE) } returns 2

        assertThrows<IllegalArgumentException> { throwExceptionWhenInvalid(bundle, 2) }
    }

    @Test
    fun `When per page higher than 200, then illegal argument exception raised`() {
        every { bundle.size() } returns 2
        every { bundle.containsKey(LANG) } returns true
        every { bundle.getString(LANG) } returns "en"
        every { bundle.containsKey(PAGE) } returns true
        every { bundle.getInt(PAGE) } returns 0
        every { bundle.containsKey(PER_PAGE) } returns true
        every { bundle.getInt(PER_PAGE) } returns 201

        assertThrows<IllegalArgumentException> { throwExceptionWhenInvalid(bundle, 2) }
    }

    @Test
    fun `When per page between 3 & 200, then no exception raised`() {
        every { bundle.size() } returns 2
        every { bundle.containsKey(LANG) } returns true
        every { bundle.getString(LANG) } returns "en"
        every { bundle.containsKey(PAGE) } returns true
        every { bundle.getInt(PAGE) } returns 1
        every { bundle.containsKey(PER_PAGE) } returns true
        every { bundle.getInt(PER_PAGE) } returns 20

        assertDoesNotThrow { throwExceptionWhenInvalid(bundle, 2) }
    }

    //}

    //    @Nested
//    @DisplayName("format to url encoded")
//    inner class InputFormat {

    @Test
    fun `When non empty single term, then return it with no change`() {
        val searchTerms = "dog"
        val expected = "dog"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))
    }

    @Test
    fun `When empty term, then return null`() {
        var searchTerms = " "
        Assert.assertNull(formatToUrlEncoded(searchTerms))
        searchTerms = "  "
        Assert.assertNull(formatToUrlEncoded(searchTerms))
        searchTerms = "        "
        Assert.assertNull(formatToUrlEncoded(searchTerms))
    }

    @Test
    fun `When non empty single term with " " prefix, then return its value without the prefix`() {
        val searchTerms = " dog"
        val expected = "dog"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))
    }

    @Test
    fun `When non empty single term with " " suffix, then return its value without the suffix`() {
        val searchTerms = "dog "
        val expected = "dog"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))
    }

    @Test
    fun `When non empty multiple term contains comma or space, then replace comma or space with +`() {
        var searchTerms = "dog,cat"
        var expected = "dog+cat"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))
        searchTerms = "dog cat"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))
        searchTerms += " puppies"
        expected += "+puppies"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))

    }

    @Test
    fun `When non empty single term contains comma, then remove comma`() {
        var searchTerms = "dog,"
        val expected = "dog"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))
        searchTerms = ",dog"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))
        searchTerms = ",dog,"
        Assert.assertEquals(expected, formatToUrlEncoded(searchTerms))
    }
    //}

    @After
    fun tearDown() {
        unmockkAll()
    }
}