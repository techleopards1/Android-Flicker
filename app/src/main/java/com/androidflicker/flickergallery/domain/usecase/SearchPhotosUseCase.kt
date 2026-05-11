package com.androidflicker.flickergallery.domain.usecase

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import javax.inject.Inject

class SearchPhotosUseCase
    @Inject
    constructor(
        private val repository: PhotoRepository,
    ) {
        suspend operator fun invoke(
            query: String,
            page: Int = 1,
        ): AppResult<List<Photo>> {
            if (query.isBlank()) {
                return AppResult.Error(AppError.Unknown("Search query cannot be empty"))
            }
            return repository.searchPhotos(query, page)
        }
    }
