package com.androidflicker.flickergallery.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.androidflicker.flickergallery.presentation.screen.detail.DetailScreen
import com.androidflicker.flickergallery.presentation.screen.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route,
    ) {
        composable(AppDestination.Home.route) {
            HomeScreen(
                onNavigateToDetails = { photoId ->
                    navController.navigateToDetail(photoId)
                },
            )
        }

        composable(
            route = AppDestination.Detail.route,
            arguments =
                listOf(
                    navArgument(AppDestination.Detail.ARG_PHOTO_ID) {
                        type = NavType.StringType
                    },
                ),
            deepLinks =
                listOf(
                    navDeepLink { uriPattern = AppDeepLinks.detailPattern() },
                ),
        ) {
            DetailScreen(
                onNavigateBack = { navController.popBackStackSafely() },
            )
        }
    }
}
