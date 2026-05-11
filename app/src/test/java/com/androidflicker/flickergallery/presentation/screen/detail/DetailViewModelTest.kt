package com.androidflicker.flickergallery.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import com.androidflicker.flickergallery.domain.usecase.GetPhotoDetailsUseCase
import com.androidflicker.flickergallery.presentation.navigation.NavRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: StubDetailRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = StubDetailRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(photoId: String = "photo-1") =
        DetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf(NavRoutes.Details.ARG_PHOTO_ID to photoId)),
            getPhotoDetailsUseCase = GetPhotoDetailsUseCase(repository),
        )

    @Test
    fun `loading is false after successful load`() =
        runTest {
            repository.detailResult = AppResult.Success(fakeDetails())
            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `successful load populates item and clears loading`() =
        runTest {
            repository.detailResult = AppResult.Success(fakeDetails())
            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNotNull(state.item)
            assertNull(state.errorMessage)
            assertFalse(state.isEmpty)
            assertEquals("Test Photo", state.item?.title)
        }

    @Test
    fun `error result sets errorMessage and clears loading`() =
        runTest {
            repository.detailResult = AppResult.Error(AppError.Network())
            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNotNull(state.errorMessage)
            assertNull(state.item)
        }

    @Test
    fun `blank photo id sets isEmpty without network call`() =
        runTest {
            val viewModel = createViewModel(photoId = "")
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state.isEmpty)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
        }

    @Test
    fun `retry reloads detail after error`() =
        runTest {
            repository.detailResult = AppResult.Error(AppError.Network())
            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()
            assertNotNull(viewModel.uiState.value.errorMessage)

            repository.detailResult = AppResult.Success(fakeDetails())
            viewModel.onEvent(DetailEvent.Retry)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertNull(state.errorMessage)
            assertNotNull(state.item)
        }
}

private fun fakeDetails() =
    PhotoDetails(
        id = "photo-1",
        title = "Test Photo",
        description = "A test description",
        imageUrl = "https://example.com/photo.jpg",
        owner = "user123",
        ownerName = "Test User",
        views = 999,
        dateUploaded = "1705276800",
        dateTaken = "2024-01-14 10:00:00",
        photoPageUrl = "https://www.flickr.com/photos/user123/photo-1/",
    )

private class StubDetailRepository : PhotoRepository {
    var detailResult: AppResult<PhotoDetails> = AppResult.Success(fakeDetails())

    override suspend fun getPhotoDetails(id: String) = detailResult

    override suspend fun searchPhotos(
        query: String,
        page: Int,
    ) = AppResult.Success(emptyList<Photo>())

    override suspend fun getRecentPhotos(page: Int) = AppResult.Success(emptyList<Photo>())

    override suspend fun getPopularPhotos(page: Int) = AppResult.Success(emptyList<Photo>())
}
