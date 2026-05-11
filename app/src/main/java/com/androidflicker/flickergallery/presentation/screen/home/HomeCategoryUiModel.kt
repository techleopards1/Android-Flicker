package com.androidflicker.flickergallery.presentation.screen.home

data class HomeCategoryUiModel(
    val id: String,
    val title: String,
    val items: List<HomeItemUiModel>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
