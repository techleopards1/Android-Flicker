package com.androidflicker.flickergallery.data.repository

import com.androidflicker.flickergallery.core.dispatcher.IoDispatcher
import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.data.remote.datasource.PhotoRemoteDataSource
import com.androidflicker.flickergallery.data.remote.mapper.toDomain
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class PhotoRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: PhotoRemoteDataSource,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : PhotoRepository {
        @Suppress("TooGenericExceptionCaught")
        override suspend fun getRecentPhotos(): AppResult<List<Photo>> =
            withContext(ioDispatcher) {
                try {
                    AppResult.Success(remoteDataSource.getRecentPhotos().toDomain())
                } catch (e: IOException) {
                    AppResult.Error(AppError.Network(e.message ?: "Network failure"))
                } catch (e: Exception) {
                    // Last-resort catch at the data-layer boundary — converts any unexpected
                    // runtime failure into a typed AppError instead of crashing the app
                    AppResult.Error(AppError.Unknown(e.message ?: "Unexpected error"))
                }
            }
    }
