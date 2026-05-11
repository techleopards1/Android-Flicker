package com.androidflicker.flickergallery.data.repository

import com.androidflicker.flickergallery.data.remote.api.ApiService
import com.androidflicker.flickergallery.domain.repository.FlickerRepository
import javax.inject.Inject

class FlickerRepositoryImpl
    @Inject
    constructor(
        private val apiService: ApiService,
    ) : FlickerRepository
