package com.androidflicker.flickergallery.data.remote.datasource

import com.androidflicker.flickergallery.core.network.NetworkConstants
import com.androidflicker.flickergallery.data.remote.api.ApiService
import com.androidflicker.flickergallery.data.remote.dto.PhotoDto
import javax.inject.Inject

class PhotoRemoteDataSource
    @Inject
    constructor(
        private val apiService: ApiService,
    ) {
        suspend fun getRecentPhotos(): List<PhotoDto> =
            apiService
                .getRecentPhotos(
                    method = "flickr.photos.getRecent",
                    apiKey = NetworkConstants.FLICKR_API_KEY,
                    format = "json",
                    noJsonCallback = 1,
                    perPage = 20,
                ).photos
                .photos
    }
