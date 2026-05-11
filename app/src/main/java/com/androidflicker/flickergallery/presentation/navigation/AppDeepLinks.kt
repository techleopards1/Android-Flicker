package com.androidflicker.flickergallery.presentation.navigation

// Custom scheme deep links: androidflicker://photo/{photoId}
// TODO: Replace with HTTPS App Links (https://<domain>/photo/{photoId}) once a
//       production domain and assetlinks.json are available.
object AppDeepLinks {
    const val SCHEME = "androidflicker"
    const val HOST = "photo"

    fun detailPattern(): String = "$SCHEME://$HOST/{${AppDestination.Detail.ARG_PHOTO_ID}}"
}
