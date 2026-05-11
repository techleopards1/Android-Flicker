package com.androidflicker.flickergallery.domain.model

data class PhotoDetails(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val owner: String,
    val ownerName: String,
    val views: Int,
    val dateUploaded: String,
    val dateTaken: String,
    val photoPageUrl: String,
    val width: Int? = null,
    val height: Int? = null,
    val sizeLabel: String? = null,
    val tags: List<String> = emptyList(),
)
