package com.androidflicker.flickergallery.domain.model

data class Photo(
    val id: String,
    val title: String,
    val imageUrl: String,
    val owner: String,
)
