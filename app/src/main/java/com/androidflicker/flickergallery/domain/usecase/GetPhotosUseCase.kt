package com.androidflicker.flickergallery.domain.usecase

import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotosUseCase
    @Inject
    constructor(
        private val repository: PhotoRepository,
    ) {
        suspend operator fun invoke(): AppResult<List<Photo>> = repository.getRecentPhotos()
    }
