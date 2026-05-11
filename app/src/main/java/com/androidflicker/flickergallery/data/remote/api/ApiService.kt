package com.androidflicker.flickergallery.data.remote.api

import com.androidflicker.flickergallery.data.remote.dto.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("services/rest/")
    suspend fun getRecentPhotos(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: Int,
        @Query("per_page") perPage: Int,
    ): FlickrResponse
}
