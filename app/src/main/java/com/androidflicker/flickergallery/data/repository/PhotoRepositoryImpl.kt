package com.androidflicker.flickergallery.data.repository

import com.androidflicker.flickergallery.core.dispatcher.IoDispatcher
import com.androidflicker.flickergallery.core.network.safeApiCall
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.data.remote.datasource.PhotoRemoteDataSource
import com.androidflicker.flickergallery.data.remote.mapper.toDomain
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: PhotoRemoteDataSource,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : PhotoRepository {
        override suspend fun searchPhotos(
            query: String,
            page: Int,
        ): AppResult<List<Photo>> =
            withContext(ioDispatcher) {
                safeApiCall { remoteDataSource.searchPhotos(query, page).toDomain() }
            }

        override suspend fun getRecentPhotos(page: Int): AppResult<List<Photo>> =
            withContext(ioDispatcher) {
                safeApiCall { remoteDataSource.getRecentPhotos(page).toDomain() }
            }

        override suspend fun getPopularPhotos(page: Int): AppResult<List<Photo>> =
            withContext(ioDispatcher) {
                safeApiCall { remoteDataSource.getPopularPhotos(page).toDomain() }
            }

        override suspend fun getPhotoDetails(id: String): AppResult<PhotoDetails> =
            withContext(ioDispatcher) {
                safeApiCall { remoteDataSource.getPhotoDetails(id).toDomain() }
            }
    }
