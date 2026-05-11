package com.androidflicker.flickergallery.presentation.screen.home

import com.androidflicker.flickergallery.domain.model.Photo

data class HomeUiState(
    val isLoading: Boolean = false,
    val photos: List<Photo> = emptyList(),
    val error: String? = null,
)
