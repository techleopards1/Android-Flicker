package com.androidflicker.flickergallery.core.network

object NetworkConstants {
    const val TIMEOUT_SECONDS = 30L

    // Obtain a key from https://www.flickr.com/services/api/
    // For production, inject via BuildConfig from a secrets file, never commit the real key
    const val FLICKR_API_KEY = ""

    const val DEFAULT_PAGE = 1
    const val DEFAULT_PER_PAGE = 20
}
