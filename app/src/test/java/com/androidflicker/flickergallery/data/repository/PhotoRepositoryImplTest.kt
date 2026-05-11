package com.androidflicker.flickergallery.data.repository

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.data.remote.api.ApiService
import com.androidflicker.flickergallery.data.remote.datasource.PhotoRemoteDataSource
import com.androidflicker.flickergallery.data.remote.dto.FlickrResponse
import com.androidflicker.flickergallery.data.remote.dto.PhotoDatesDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoDetailsDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoDetailsResponseDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoOwnerDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoTextDto
import com.androidflicker.flickergallery.data.remote.dto.PhotosPageDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class PhotoRepositoryImplTest {
    private fun repository(apiService: ApiService) =
        PhotoRepositoryImpl(
            remoteDataSource = PhotoRemoteDataSource(apiService),
            ioDispatcher = Dispatchers.Unconfined,
        )

    // ── getRecentPhotos ───────────────────────────────────────────────────────

    @Test
    fun `getRecentPhotos returns Success with mapped domain models`() =
        runBlocking {
            val result = repository(FakeApiService(photos = listOf(stubPhotoDto()))).getRecentPhotos()
            assertTrue(result is AppResult.Success)
            assertEquals(1, (result as AppResult.Success).data.size)
            assertEquals("photo-1", result.data[0].id)
        }

    @Test
    fun `getRecentPhotos maps title correctly`() =
        runBlocking {
            val result = repository(FakeApiService(photos = listOf(stubPhotoDto(title = "Sunset")))).getRecentPhotos()
            assertEquals("Sunset", (result as AppResult.Success).data[0].title)
        }

    @Test
    fun `getRecentPhotos returns Network error on IOException`() =
        runBlocking {
            val result = repository(FakeApiService(exception = IOException("timeout"))).getRecentPhotos()
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Network)
        }

    @Test
    fun `getRecentPhotos returns Timeout error on SocketTimeoutException`() =
        runBlocking {
            val result = repository(FakeApiService(exception = SocketTimeoutException())).getRecentPhotos()
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Timeout)
        }

    @Test
    fun `getRecentPhotos returns empty Success for empty API response`() =
        runBlocking {
            val result = repository(FakeApiService(photos = emptyList())).getRecentPhotos()
            assertTrue(result is AppResult.Success)
            assertTrue((result as AppResult.Success).data.isEmpty())
        }

    // ── searchPhotos ──────────────────────────────────────────────────────────

    @Test
    fun `searchPhotos returns Success with results`() =
        runBlocking {
            val result = repository(FakeApiService(photos = listOf(stubPhotoDto()))).searchPhotos("cats")
            assertTrue(result is AppResult.Success)
            assertEquals(1, (result as AppResult.Success).data.size)
        }

    @Test
    fun `searchPhotos returns Network error on failure`() =
        runBlocking {
            val result = repository(FakeApiService(exception = IOException())).searchPhotos("cats")
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Network)
        }

    // ── getPopularPhotos ──────────────────────────────────────────────────────

    @Test
    fun `getPopularPhotos returns Success with results`() =
        runBlocking {
            val result = repository(FakeApiService(photos = listOf(stubPhotoDto(), stubPhotoDto("photo-2")))).getPopularPhotos()
            assertTrue(result is AppResult.Success)
            assertEquals(2, (result as AppResult.Success).data.size)
        }

    // ── getPhotoDetails ───────────────────────────────────────────────────────

    @Test
    fun `getPhotoDetails returns Success with mapped domain model`() =
        runBlocking {
            val result = repository(FakeApiService()).getPhotoDetails("photo-1")
            assertTrue(result is AppResult.Success)
            assertEquals("photo-1", (result as AppResult.Success).data.id)
        }

    @Test
    fun `getPhotoDetails returns Network error on failure`() =
        runBlocking {
            val result = repository(FakeApiService(exception = IOException())).getPhotoDetails("photo-1")
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Network)
        }

    // ── DTO is never exposed ──────────────────────────────────────────────────

    @Test
    fun `repository result contains domain Photo not DTO`() =
        runBlocking {
            val result = repository(FakeApiService(photos = listOf(stubPhotoDto()))).getRecentPhotos()
            // Compile-time proof: result.data is List<Photo>, not List<PhotoDto>
            val photos: List<com.androidflicker.flickergallery.domain.model.Photo> =
                (result as AppResult.Success).data
            assertEquals(1, photos.size)
        }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun stubPhotoDto(
    id: String = "photo-1",
    title: String = "Test Photo",
) = PhotoDto(id = id, owner = "owner@flickr", secret = "secret1", server = "65535", farm = 1, title = title)

private fun stubPhotoDetailsResponseDto(id: String = "photo-1") =
    PhotoDetailsResponseDto(
        photo =
            PhotoDetailsDto(
                id = id,
                secret = "secret1",
                server = "65535",
                title = PhotoTextDto("Details Title"),
                description = PhotoTextDto("A description"),
                owner = PhotoOwnerDto(nsid = "owner@flickr", username = "user", realname = "Real User"),
                dates = PhotoDatesDto(posted = "1700000000", taken = "2023-11-01"),
                views = "10",
            ),
        stat = "ok",
    )

private fun stubFlickrResponse(photos: List<PhotoDto>) =
    FlickrResponse(
        photos = PhotosPageDto(photos = photos, page = 1, pages = 1, perPage = 20, total = photos.size),
        stat = "ok",
    )

private class FakeApiService(
    private val photos: List<PhotoDto> = emptyList(),
    private val exception: Exception? = null,
) : ApiService {
    override suspend fun searchPhotos(
        method: String,
        apiKey: String,
        query: String,
        format: String,
        noJsonCallback: Int,
        perPage: Int,
        page: Int,
    ): FlickrResponse {
        exception?.let { throw it }
        return stubFlickrResponse(photos)
    }

    override suspend fun getRecentPhotos(
        method: String,
        apiKey: String,
        format: String,
        noJsonCallback: Int,
        perPage: Int,
        page: Int,
    ): FlickrResponse {
        exception?.let { throw it }
        return stubFlickrResponse(photos)
    }

    override suspend fun getPopularPhotos(
        method: String,
        apiKey: String,
        format: String,
        noJsonCallback: Int,
        perPage: Int,
        page: Int,
    ): FlickrResponse {
        exception?.let { throw it }
        return stubFlickrResponse(photos)
    }

    override suspend fun getPhotoDetails(
        method: String,
        apiKey: String,
        photoId: String,
        format: String,
        noJsonCallback: Int,
    ): PhotoDetailsResponseDto {
        exception?.let { throw it }
        return stubPhotoDetailsResponseDto(photoId)
    }
}
