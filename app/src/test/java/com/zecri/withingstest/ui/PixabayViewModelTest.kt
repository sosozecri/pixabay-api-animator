package com.zecri.withingstest.ui

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zecri.withingstest.data.model.image.PixabayImage
import com.zecri.withingstest.data.source.PixabayMediaRepository
import com.zecri.withingstest.data.source.remote.Q
import com.zecri.withingstest.util.EmptyResponseException
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class PixabayViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    lateinit var repository: PixabayMediaRepository

    @MockK
    lateinit var parameters: Bundle

    @RelaxedMockK
    lateinit var imagesLoadingObserver: Observer<Result<List<PixabayImage>?>>

    lateinit var pixabayViewModel: PixabayViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        pixabayViewModel = PixabayViewModel(repository)
        pixabayViewModel.imagesResult.observeForever(imagesLoadingObserver)
        val searchParameters = "husky"
        every { parameters.getString(Q) } returns searchParameters
    }

    @Test
    fun `When repository returns empty, then failure result is sent to observers`() =
        testCoroutineRule.runBlockingTest {
            val emptyList = arrayListOf<PixabayImage>()
            coEvery { repository.getImages(parameters) } returns emptyList
            val captor = slot<Result<List<PixabayImage>?>>()

            pixabayViewModel.loadImages(parameters)

            verify { imagesLoadingObserver.onChanged(capture(captor)) }
            assertTrue(captor.captured.isFailure)
            assertTrue(captor.captured.exceptionOrNull() is EmptyResponseException)
        }

    @Test
    fun `When repository call raises an exception, then failure result is sent to observers`() =
        testCoroutineRule.runBlockingTest {
            val exception = IllegalArgumentException()
            coEvery { repository.getImages(parameters) } throws exception
            val captor = slot<Result<List<PixabayImage>?>>()

            pixabayViewModel.loadImages(parameters)

            verify { imagesLoadingObserver.onChanged(capture(captor)) }
            assertTrue(captor.captured.isFailure)
            assertTrue(captor.captured.exceptionOrNull() is IllegalArgumentException)
        }

    @Test
    fun `When repository returns non empty, then success result is sent to observers`() =
        testCoroutineRule.runBlockingTest {
            val result = arrayListOf(PixabayImage(1L))
            coEvery { repository.getImages(parameters) } returns result
            val captor = slot<Result<List<PixabayImage>?>>()

            pixabayViewModel.loadImages(parameters)

            verify { imagesLoadingObserver.onChanged(capture(captor)) }
            assertTrue(captor.captured.isSuccess)
            assertTrue(captor.captured.getOrNull()?.size == 1)
        }

    @Test
    fun `When selected image already present in list, then remove it from the list`() {
        val image = PixabayImage(1L)
        pixabayViewModel.selectedImages.value = arrayListOf(image)
        val imagesSelectionObserver = mockk<Observer<List<PixabayImage>?>>(relaxed = true)
        pixabayViewModel.selectedImages.observeForever(imagesSelectionObserver)
        val captor = slot<List<PixabayImage>>()

        val isSelected = pixabayViewModel.switchSelectionState(image)

        assertFalse(isSelected)
        verify { imagesSelectionObserver.onChanged(capture(captor)) }
        assertFalse(captor.captured.contains(image))
        pixabayViewModel.selectedImages.removeObserver(imagesSelectionObserver)
    }

    @Test
    fun `When selected image not in list, then add it to the list`() {
        val image = PixabayImage(1L)
        pixabayViewModel.selectedImages.value = arrayListOf()
        val imagesSelectionObserver = mockk<Observer<List<PixabayImage>?>>(relaxed = true)
        pixabayViewModel.selectedImages.observeForever(imagesSelectionObserver)
        val captor = slot<List<PixabayImage>>()

        val isSelected = pixabayViewModel.switchSelectionState(image)

        assertTrue(isSelected)
        verify { imagesSelectionObserver.onChanged(capture(captor)) }
        assertTrue(captor.captured.contains(image))
        pixabayViewModel.selectedImages.removeObserver(imagesSelectionObserver)
    }

    @After
    fun tearDown() {
        pixabayViewModel.imagesResult.removeObserver(imagesLoadingObserver)
        unmockkAll()
    }

}