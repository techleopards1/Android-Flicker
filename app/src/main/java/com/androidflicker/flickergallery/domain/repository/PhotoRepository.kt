package com.androidflicker.flickergallery.domain.repository

import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails

interface PhotoRepository {
    suspend fun searchPhotos(
        query: String,
        page: Int = 1,
    ): AppResult<List<Photo>>

    suspend fun getRecentPhotos(page: Int = 1): AppResult<List<Photo>>

    suspend fun getPopularPhotos(page: Int = 1): AppResult<List<Photo>>

    suspend fun getPhotoDetails(id: String): AppResult<PhotoDetails>
}
