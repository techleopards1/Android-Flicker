package com.androidflicker.flickergallery.data.remote.datasource

import com.androidflicker.flickergallery.core.network.NetworkConstants
import com.androidflicker.flickergallery.data.remote.api.ApiService
import com.androidflicker.flickergallery.data.remote.dto.PhotoDetailsDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoDto
import javax.inject.Inject

class PhotoRemoteDataSource
    @Inject
    constructor(
        private val apiService: ApiService,
    ) {
        suspend fun searchPhotos(
            query: String,
            page: Int,
        ): List<PhotoDto> =
            apiService
                .searchPhotos(
                    method = METHOD_SEARCH,
                    apiKey = NetworkConstants.FLICKR_API_KEY,
                    query = query,
                    format = FORMAT_JSON,
                    noJsonCallback = NO_JSON_CALLBACK,
                    perPage = NetworkConstants.DEFAULT_PER_PAGE,
                    page = page,
                ).photos
                .photos

        suspend fun getRecentPhotos(page: Int): List<PhotoDto> =
            apiService
                .getRecentPhotos(
                    method = METHOD_RECENT,
                    apiKey = NetworkConstants.FLICKR_API_KEY,
                    format = FORMAT_JSON,
                    noJsonCallback = NO_JSON_CALLBACK,
                    perPage = NetworkConstants.DEFAULT_PER_PAGE,
                    page = page,
                ).photos
                .photos

        suspend fun getPopularPhotos(page: Int): List<PhotoDto> =
            apiService
                .getPopularPhotos(
                    method = METHOD_POPULAR,
                    apiKey = NetworkConstants.FLICKR_API_KEY,
                    format = FORMAT_JSON,
                    noJsonCallback = NO_JSON_CALLBACK,
                    perPage = NetworkConstants.DEFAULT_PER_PAGE,
                    page = page,
                ).photos
                .photos

        suspend fun getPhotoDetails(id: String): PhotoDetailsDto =
            apiService
                .getPhotoDetails(
                    method = METHOD_DETAILS,
                    apiKey = NetworkConstants.FLICKR_API_KEY,
                    photoId = id,
                    format = FORMAT_JSON,
                    noJsonCallback = NO_JSON_CALLBACK,
                ).photo

        private companion object {
            const val METHOD_SEARCH = "flickr.photos.search"
            const val METHOD_RECENT = "flickr.photos.getRecent"
            const val METHOD_POPULAR = "flickr.interestingness.getList"
            const val METHOD_DETAILS = "flickr.photos.getInfo"
            const val FORMAT_JSON = "json"
            const val NO_JSON_CALLBACK = 1
        }
    }
