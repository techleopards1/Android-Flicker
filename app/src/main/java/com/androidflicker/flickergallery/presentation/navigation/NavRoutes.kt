package com.androidflicker.flickergallery.presentation.navigation

sealed class NavRoutes(
    val route: String,
) {
    data object Home : NavRoutes("home")

    data object Details : NavRoutes("details/{photoId}") {
        const val ARG_PHOTO_ID = "photoId"

        fun createRoute(photoId: String) = "details/$photoId"
    }
}
