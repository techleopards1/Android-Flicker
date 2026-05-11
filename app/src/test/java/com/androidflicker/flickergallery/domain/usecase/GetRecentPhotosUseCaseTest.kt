package com.androidflicker.flickergallery.domain.usecase

import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetRecentPhotosUseCaseTest {
    private val repository = RecordingPhotoRepository()
    private val useCase = GetRecentPhotosUseCase(repository)

    @Test
    fun `invoke delegates to repository getRecentPhotos`() =
        runBlocking {
            val result = useCase()
            assertTrue(result is AppResult.Success)
        }

    @Test
    fun `invoke passes default page 1`() =
        runBlocking {
            useCase()
            assertEquals(1, repository.lastRecentPage)
        }

    @Test
    fun `invoke passes custom page number`() =
        runBlocking {
            useCase(page = 3)
            assertEquals(3, repository.lastRecentPage)
        }

    @Test
    fun `invoke returns repository result unchanged`() =
        runBlocking {
            val result = useCase()
            assertEquals(repository.recentPhotosResult, result)
        }
}

// Distinct name to avoid collision with FakePhotoRepository in SearchPhotosUseCaseTest
private class RecordingPhotoRepository : PhotoRepository {
    var lastRecentPage: Int = -1
    val recentPhotosResult: AppResult<List<Photo>> = AppResult.Success(emptyList())

    override suspend fun searchPhotos(
        query: String,
        page: Int,
    ) = AppResult.Success(emptyList<Photo>())

    override suspend fun getRecentPhotos(page: Int): AppResult<List<Photo>> {
        lastRecentPage = page
        return recentPhotosResult
    }

    override suspend fun getPopularPhotos(page: Int) = AppResult.Success(emptyList<Photo>())

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
