package com.androidflicker.flickergallery.domain.usecase

import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPopularPhotosUseCase
    @Inject
    constructor(
        private val repository: PhotoRepository,
    ) {
        suspend operator fun invoke(page: Int = 1): AppResult<List<Photo>> = repository.getPopularPhotos(page)
    }
