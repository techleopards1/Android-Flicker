package com.androidflicker.flickergallery.core.config

import com.androidflicker.flickergallery.BuildConfig

object EnvironmentConfig {
    val apiBaseUrl: String = BuildConfig.API_BASE_URL
    val environment: Environment = Environment.current()
    val enableLogging: Boolean = BuildConfig.ENABLE_LOGGING
    val isDebug: Boolean = BuildConfig.DEBUG
}
