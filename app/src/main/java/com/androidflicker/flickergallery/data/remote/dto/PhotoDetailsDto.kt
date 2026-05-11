package com.androidflicker.flickergallery.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PhotoDetailsResponseDto(
    @SerializedName("photo") val photo: PhotoDetailsDto,
    @SerializedName("stat") val stat: String,
)

data class PhotoDetailsDto(
    @SerializedName("id") val id: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("title") val title: PhotoTextDto,
    @SerializedName("description") val description: PhotoTextDto,
    @SerializedName("owner") val owner: PhotoOwnerDto,
    @SerializedName("dates") val dates: PhotoDatesDto,
    @SerializedName("views") val views: String,
)

data class PhotoTextDto(
    @SerializedName("_content") val content: String,
)

data class PhotoOwnerDto(
    @SerializedName("nsid") val nsid: String,
    @SerializedName("username") val username: String,
    @SerializedName("realname") val realname: String,
)

data class PhotoDatesDto(
    @SerializedName("posted") val posted: String,
    @SerializedName("taken") val taken: String,
)
