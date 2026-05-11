package com.androidflicker.flickergallery.presentation.screen.detail

data class DetailUiState(
    val isLoading: Boolean = false,
    val item: DetailUiModel? = null,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
