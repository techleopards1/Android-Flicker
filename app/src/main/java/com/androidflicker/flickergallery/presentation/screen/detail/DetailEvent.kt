package com.androidflicker.flickergallery.presentation.screen.detail

sealed class DetailEvent {
    data object Retry : DetailEvent()
}
