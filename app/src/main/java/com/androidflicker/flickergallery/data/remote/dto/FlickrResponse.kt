package com.androidflicker.flickergallery.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FlickrResponse(
    @SerializedName("photos") val photos: PhotosPageDto,
    @SerializedName("stat") val stat: String,
)

data class PhotosPageDto(
    @SerializedName("photo") val photos: List<PhotoDto>,
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perPage: Int,
    @SerializedName("total") val total: Int,
)
