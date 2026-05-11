package com.androidflicker.flickergallery.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateToDetail(photoId: String) {
    navigate(AppDestination.Detail.createRoute(photoId))
}

fun NavController.navigateSingleTopTo(route: String) {
    navigate(route) {
        launchSingleTop = true
    }
}

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun NavController.safeNavigate(route: String) {
    try {
        navigate(route)
    } catch (e: Exception) {
        // Intentional no-op: route not found or invalid state — prevents crash on bad navigation
    }
}

fun NavController.popBackStackSafely(): Boolean = if (previousBackStackEntry != null) popBackStack() else false

fun NavController.navigateToDetailSingleTop(photoId: String) {
    navigate(
        route = AppDestination.Detail.createRoute(photoId),
        navOptions =
            NavOptions
                .Builder()
                .setLaunchSingleTop(true)
                .build(),
    )
}
