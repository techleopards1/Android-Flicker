package com.androidflicker.flickergallery.presentation.screen.home

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.HomeCategory
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import com.androidflicker.flickergallery.domain.usecase.GetHomeCategoriesUseCase
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

    private fun createViewModel() = HomeViewModel(GetHomeCategoriesUseCase(repository))

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
    fun `successful load populates all 10 category rows`() =
        runTest {
            val photo = Photo(id = "1", title = "Title", imageUrl = "url", owner = "owner")
            repository.popularResult = AppResult.Success(listOf(photo))
            repository.recentResult = AppResult.Success(listOf(photo))
            repository.searchResult = AppResult.Success(listOf(photo))

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertEquals(HomeCategory.entries.size, state.categories.size)
            assertNull(state.errorMessage)
            assertFalse(state.isEmpty)
        }

    @Test
    fun `all categories error shows screen-level error message`() =
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
            assertFalse(state.isEmpty)
        }

    @Test
    fun `all categories return empty shows isEmpty true`() =
        runTest {
            repository.popularResult = AppResult.Success(emptyList())
            repository.recentResult = AppResult.Success(emptyList())
            repository.searchResult = AppResult.Success(emptyList())

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertTrue(state.isEmpty)
            assertNull(state.errorMessage)
        }

    @Test
    fun `some categories have items isEmpty is false`() =
        runTest {
            val photo = Photo(id = "1", title = "Title", imageUrl = "url", owner = "owner")
            repository.popularResult = AppResult.Success(listOf(photo))
            repository.recentResult = AppResult.Success(emptyList())
            repository.searchResult = AppResult.Success(emptyList())

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isEmpty)
        }

    @Test
    fun `partial error shows row-level error without screen error`() =
        runTest {
            val photo = Photo(id = "2", title = "Photo", imageUrl = "url", owner = "owner")
            repository.popularResult = AppResult.Error(AppError.Network())
            repository.recentResult = AppResult.Success(listOf(photo))
            repository.searchResult = AppResult.Success(listOf(photo))

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
            val popular = state.categories.find { it.id == HomeCategory.POPULAR.id }
            assertNotNull(popular?.errorMessage)
            assertTrue(popular?.items?.isEmpty() == true)
            val recent = state.categories.find { it.id == HomeCategory.RECENT.id }
            assertEquals(1, recent?.items?.size)
        }

    @Test
    fun `retry reloads all categories`() =
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
            assertNull(state.errorMessage)
            assertFalse(state.isEmpty)
            state.categories.forEach { assertTrue(it.items.isNotEmpty()) }
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

            val popular =
                viewModel.uiState.value.categories
                    .find { it.id == HomeCategory.POPULAR.id }
            val item = popular?.items?.first()
            assertEquals("42", item?.id)
            assertEquals("Test Title", item?.title)
            assertEquals("http://img", item?.imageUrl)
            assertEquals("Test Owner", item?.subtitle)
        }

    @Test
    fun `categories list contains all required category ids`() =
        runTest {
            repository.popularResult = AppResult.Success(emptyList())
            repository.recentResult = AppResult.Success(emptyList())
            repository.searchResult = AppResult.Success(emptyList())

            val viewModel = createViewModel()
            testDispatcher.scheduler.advanceUntilIdle()

            val ids =
                viewModel.uiState.value.categories
                    .map { it.id }
                    .toSet()
            val required =
                setOf(
                    "nature",
                    "animals",
                    "architecture",
                    "historical",
                    "popular",
                    "trending",
                    "technology",
                    "space",
                    "travel",
                    "recent",
                )
            assertTrue(ids.containsAll(required))
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
