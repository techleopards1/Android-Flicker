package com.androidflicker.flickergallery.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.androidflicker.flickergallery.presentation.screen.detail.DetailScreen
import com.androidflicker.flickergallery.presentation.screen.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onNavigateToDetails = { photoId ->
                    navController.navigate(NavRoutes.Details.createRoute(photoId))
                },
            )
        }

        composable(
            route = NavRoutes.Details.route,
            arguments =
                listOf(
                    navArgument(NavRoutes.Details.ARG_PHOTO_ID) {
                        type = NavType.StringType
                    },
                ),
        ) {
            DetailScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
