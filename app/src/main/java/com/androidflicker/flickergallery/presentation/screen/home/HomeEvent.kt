package com.androidflicker.flickergallery.presentation.screen.home

sealed class HomeEvent {
    data object LoadContent : HomeEvent()

    data object RetryLoad : HomeEvent()
}
