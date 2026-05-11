package com.androidflicker.flickergallery.presentation.screen.home

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import com.androidflicker.flickergallery.domain.usecase.GetPopularPhotosUseCase
import com.androidflicker.flickergallery.domain.usecase.GetRecentPhotosUseCase
import com.androidflicker.flickergallery.domain.usecase.SearchPhotosUseCase
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
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: StubPhotoRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = StubPhotoRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() =
        HomeViewModel(
            getRecentPhotosUseCase = GetRecentPhotosUseCase(repository),
            getPopularPhotosUseCase = GetPopularPhotosUseCase(repository),
            searchPhotosUseCase = SearchPhotosUseCase(repository),
        )

    @Test
    fun `after load completes loading is false`() =
        runTest {
            repository.popularResult = AppResult.Success(emptyList())
            repository.recentResult = AppResult.Success(emptyList())
            repository.searchResult = AppResult.Success(emptyList())

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `successful load populates all sections`() =
        runTest {
            val photo = Photo(id = "1", title = "Title", imageUrl = "url", owner = "owner")
            repository.popularResult = AppResult.Success(listOf(photo))
            repository.recentResult = AppResult.Success(listOf(photo))
            repository.searchResult = AppResult.Success(listOf(photo))

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertEquals(1, state.popularItems.size)
            assertEquals(1, state.recentItems.size)
            assertEquals(1, state.trendingItems.size)
            assertNull(state.errorMessage)
        }

    @Test
    fun `all sections error shows error message`() =
        runTest {
            val error = AppError.Network()
            repository.popularResult = AppResult.Error(error)
            repository.recentResult = AppResult.Error(error)
            repository.searchResult = AppResult.Error(error)

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNotNull(state.errorMessage)
            assertTrue(state.popularItems.isEmpty())
            assertTrue(state.recentItems.isEmpty())
        }

    @Test
    fun `partial error still shows available sections`() =
        runTest {
            val photo = Photo(id = "2", title = "Photo", imageUrl = "url", owner = "owner")
            repository.popularResult = AppResult.Error(AppError.Network())
            repository.recentResult = AppResult.Success(listOf(photo))
            repository.searchResult = AppResult.Success(listOf(photo))

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertTrue(state.popularItems.isEmpty())
            assertEquals(1, state.recentItems.size)
            assertEquals(1, state.trendingItems.size)
            assertNull(state.errorMessage)
        }

    @Test
    fun `retry reloads content`() =
        runTest {
            repository.popularResult = AppResult.Error(AppError.Network())
            repository.recentResult = AppResult.Error(AppError.Network())
            repository.searchResult = AppResult.Error(AppError.Network())

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val photo = Photo(id = "3", title = "Photo", imageUrl = "url", owner = "owner")
            repository.popularResult = AppResult.Success(listOf(photo))
            repository.recentResult = AppResult.Success(listOf(photo))
            repository.searchResult = AppResult.Success(listOf(photo))

            viewModel.onEvent(HomeEvent.RetryLoad)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertEquals(1, state.popularItems.size)
            assertNull(state.errorMessage)
        }

    @Test
    fun `photo maps to ui model correctly`() =
        runTest {
            val photo = Photo(id = "42", title = "Test Title", imageUrl = "http://img", owner = "Test Owner")
            repository.popularResult = AppResult.Success(listOf(photo))
            repository.recentResult = AppResult.Success(emptyList())
            repository.searchResult = AppResult.Success(emptyList())

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val item =
                viewModel.uiState.value.popularItems
                    .first()
            assertEquals("42", item.id)
            assertEquals("Test Title", item.title)
            assertEquals("http://img", item.imageUrl)
            assertEquals("Test Owner", item.subtitle)
        }
}

private class StubPhotoRepository : PhotoRepository {
    var popularResult: AppResult<List<Photo>> = AppResult.Success(emptyList())
    var recentResult: AppResult<List<Photo>> = AppResult.Success(emptyList())
    var searchResult: AppResult<List<Photo>> = AppResult.Success(emptyList())

    override suspend fun getPopularPhotos(page: Int) = popularResult

    override suspend fun getRecentPhotos(page: Int) = recentResult

    override suspend fun searchPhotos(
        query: String,
        page: Int,
    ) = searchResult

    override suspend fun getPhotoDetails(id: String) =
        AppResult.Success(
            PhotoDetails(
                id = id,
                title = "",
                description = "",
                imageUrl = "",
                owner = "",
                ownerName = "",
                views = 0,
                dateUploaded = "",
                dateTaken = "",
                photoPageUrl = "",
            ),
        )
}
