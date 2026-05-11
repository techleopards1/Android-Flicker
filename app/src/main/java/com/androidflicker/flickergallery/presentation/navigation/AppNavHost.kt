package com.androidflicker.flickergallery.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
        ) { backStackEntry ->
            val photoId =
                backStackEntry.arguments?.getString(NavRoutes.Details.ARG_PHOTO_ID).orEmpty()
            DetailsPlaceholder(photoId = photoId)
        }
    }
}

@Composable
private fun DetailsPlaceholder(photoId: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Details screen coming soon\nPhoto ID: $photoId",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
