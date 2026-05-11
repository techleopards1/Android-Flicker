package com.androidflicker.flickergallery.presentation.screen.detail

data class DetailUiModel(
    val id: String,
    val title: String,
    val imageUrl: String,
    val description: String,
    val authorName: String,
    val views: String,
    val dateUploaded: String,
    val dateTaken: String,
    val photoPageUrl: String,
)
