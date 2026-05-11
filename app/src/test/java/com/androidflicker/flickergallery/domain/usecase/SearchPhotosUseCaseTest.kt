package com.androidflicker.flickergallery.domain.usecase

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchPhotosUseCaseTest {
    private val useCase = SearchPhotosUseCase(FakePhotoRepository())

    @Test
    fun `blank query returns Unknown error`() =
        runBlocking {
            val result = useCase(query = "")
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Unknown)
        }

    @Test
    fun `whitespace-only query returns Unknown error`() =
        runBlocking {
            val result = useCase(query = "   ")
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Unknown)
        }

    @Test
    fun `valid query delegates to repository and returns Success`() =
        runBlocking {
            val result = useCase(query = "cats")
            assertTrue(result is AppResult.Success)
        }

    @Test
    fun `valid query with page delegates to repository`() =
        runBlocking {
            val result = useCase(query = "dogs", page = 2)
            assertTrue(result is AppResult.Success)
        }
}

private class FakePhotoRepository : PhotoRepository {
    override suspend fun searchPhotos(
        query: String,
        page: Int,
    ) = AppResult.Success(emptyList<Photo>())

    override suspend fun getRecentPhotos(page: Int) = AppResult.Success(emptyList<Photo>())

    override suspend fun getPopularPhotos(page: Int) = AppResult.Success(emptyList<Photo>())

    override suspend fun getPhotoDetails(id: String) =
        AppResult.Success(
            PhotoDetails(
                id = id,
                title = "Test",
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
