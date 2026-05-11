package com.androidflicker.flickergallery.data.remote.api

import com.androidflicker.flickergallery.data.remote.dto.FlickrResponse
import com.androidflicker.flickergallery.data.remote.dto.PhotoDetailsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("services/rest/")
    suspend fun searchPhotos(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("text") query: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: Int,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): FlickrResponse

    @GET("services/rest/")
    suspend fun getRecentPhotos(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: Int,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): FlickrResponse

    @GET("services/rest/")
    suspend fun getPopularPhotos(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: Int,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): FlickrResponse

    @GET("services/rest/")
    suspend fun getPhotoDetails(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("photo_id") photoId: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJsonCallback: Int,
    ): PhotoDetailsResponseDto
}
