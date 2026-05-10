package com.androidflicker.flickergallery.data.remote.api

import retrofit2.http.GET

interface ApiService {
    // Placeholder — add real endpoints as features are implemented
    @GET("services/rest/")
    suspend fun getPlaceholder(): Any
}
