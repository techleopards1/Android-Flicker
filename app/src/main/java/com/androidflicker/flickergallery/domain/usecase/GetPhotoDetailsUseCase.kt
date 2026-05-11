package com.androidflicker.flickergallery.domain.usecase

import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import com.androidflicker.flickergallery.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotoDetailsUseCase
    @Inject
    constructor(
        private val repository: PhotoRepository,
    ) {
        suspend operator fun invoke(id: String): AppResult<PhotoDetails> = repository.getPhotoDetails(id)
    }
