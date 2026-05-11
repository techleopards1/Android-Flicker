package com.androidflicker.flickergallery.presentation.screen.home

data class HomeUiState(
    val isLoading: Boolean = false,
    val popularItems: List<HomeItemUiModel> = emptyList(),
    val recentItems: List<HomeItemUiModel> = emptyList(),
    val trendingItems: List<HomeItemUiModel> = emptyList(),
    val errorMessage: String? = null,
)
