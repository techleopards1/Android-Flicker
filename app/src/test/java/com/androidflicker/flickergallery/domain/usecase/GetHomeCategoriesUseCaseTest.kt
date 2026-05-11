package com.androidflicker.flickergallery.domain.usecase

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.HomeCategory
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetHomeCategoriesUseCaseTest {
    private val photo = Photo(id = "1", title = "Title", imageUrl = "url", owner = "owner")
    private val repository = StubCategoryRepository(photo)
    private val useCase = GetHomeCategoriesUseCase(repository)

    @Test
    fun `returns result for all 10 categories`() =
        runBlocking {
            val results = useCase()
            assertEquals(HomeCategory.entries.size, results.size)
        }

    @Test
    fun `popular category uses getPopularPhotos`() =
        runBlocking {
            val results = useCase()
            val popular = results.find { it.first == HomeCategory.POPULAR }
            assertTrue(popular?.second is AppResult.Success)
            assertEquals(1, (popular?.second as AppResult.Success).data.size)
        }

    @Test
    fun `recent category uses getRecentPhotos`() =
        runBlocking {
            val results = useCase()
            val recent = results.find { it.first == HomeCategory.RECENT }
            assertTrue(recent?.second is AppResult.Success)
        }

    @Test
    fun `search categories use searchPhotos with category id`() =
        runBlocking {
            val results = useCase()
            val nature = results.find { it.first == HomeCategory.NATURE }
            assertTrue(nature?.second is AppResult.Success)
            assertTrue(HomeCategory.NATURE.id in repository.searchQueries)
        }

    @Test
    fun `repository errors propagate per category`() =
        runBlocking {
            val failingRepo = StubCategoryRepository(photo, searchError = AppError.Network())
            val results = GetHomeCategoriesUseCase(failingRepo)()
            val nature = results.find { it.first == HomeCategory.NATURE }
            assertTrue(nature?.second is AppResult.Error)
        }
}

private class StubCategoryRepository(
    private val photo: Photo,
    private val searchError: AppError? = null,
) : PhotoRepository {
    val searchQueries = mutableListOf<String>()

    override suspend fun searchPhotos(
        query: String,
        page: Int,
    ): AppResult<List<Photo>> {
        synchronized(searchQueries) { searchQueries.add(query) }
        return if (searchError != null) AppResult.Error(searchError) else AppResult.Success(listOf(photo))
    }

    override suspend fun getRecentPhotos(page: Int): AppResult<List<Photo>> = AppResult.Success(listOf(photo))

    override suspend fun getPopularPhotos(page: Int): AppResult<List<Photo>> = AppResult.Success(listOf(photo))

    override suspend fun getPhotoDetails(id: String): AppResult<PhotoDetails> =
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
