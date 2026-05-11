package com.androidflicker.flickergallery.presentation.screen.detail

internal const val DEFAULT_ASPECT_RATIO = 16f / 9f

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
    val dimensionsLabel: String? = null,
    val sizeLabel: String? = null,
    val imageAspectRatio: Float = DEFAULT_ASPECT_RATIO,
    val tags: List<String> = emptyList(),
)
