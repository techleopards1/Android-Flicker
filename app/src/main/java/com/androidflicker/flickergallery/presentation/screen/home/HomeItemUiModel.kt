package com.androidflicker.flickergallery.presentation.screen.home

data class HomeItemUiModel(
    val id: String,
    val title: String,
    val imageUrl: String,
    val subtitle: String? = null,
)
