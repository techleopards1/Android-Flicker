package com.androidflicker.flickergallery.presentation.screen.home

sealed class HomeEvent {
    data object LoadPhotos : HomeEvent()

    data object RetryLoad : HomeEvent()
}
