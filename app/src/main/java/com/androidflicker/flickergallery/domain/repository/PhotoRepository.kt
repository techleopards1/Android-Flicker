package com.androidflicker.flickergallery.domain.repository

import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo

interface PhotoRepository {
    suspend fun getRecentPhotos(): AppResult<List<Photo>>
}
