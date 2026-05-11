package com.androidflicker.flickergallery.presentation.navigation

sealed class NavRoutes(
    val route: String,
) {
    data object Home : NavRoutes("home")
}
