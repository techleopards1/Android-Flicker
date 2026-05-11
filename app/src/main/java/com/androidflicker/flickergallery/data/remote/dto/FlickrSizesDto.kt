package com.androidflicker.flickergallery.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FlickrSizesResponseDto(
    @SerializedName("sizes") val sizes: FlickrSizesContainerDto,
    @SerializedName("stat") val stat: String,
)

data class FlickrSizesContainerDto(
    @SerializedName("size") val sizes: List<FlickrSizeDto>,
)

data class FlickrSizeDto(
    @SerializedName("label") val label: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("source") val source: String,
    @SerializedName("media") val media: String,
)
