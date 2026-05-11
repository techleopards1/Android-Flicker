package com.androidflicker.flickergallery.presentation.navigation

import java.net.URLEncoder

sealed interface AppDestination {
    val route: String

    data object Home : AppDestination {
        override val route = "home"
    }

    data object Detail : AppDestination {
        const val ARG_PHOTO_ID = "photoId"
        override val route = "detail/{$ARG_PHOTO_ID}"

        fun createRoute(photoId: String): String = "detail/${URLEncoder.encode(photoId, Charsets.UTF_8.name()).replace("+", "%20")}"
    }
}
