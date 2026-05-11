package com.androidflicker.flickergallery.core.config

import com.androidflicker.flickergallery.BuildConfig

enum class
Environment(val id: String) {
    DEV("dev"),
    STAGING("staging"),
    PRODUCTION("production");

    companion object {
        fun current(): Environment = entries.firstOrNull {
            it.id == BuildConfig.APP_ENVIRONMENT
        } ?: PRODUCTION
    }
}
