package com.androidflicker.flickergallery.presentation.screen.home

data class HomeUiState(
    val isLoading: Boolean = false,
    val categories: List<HomeCategoryUiModel> = emptyList(),
    val isEmpty: Boolean = false,
    val errorMessage: String? = null,
)
