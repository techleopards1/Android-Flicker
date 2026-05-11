package com.androidflicker.flickergallery.core.result

import com.androidflicker.flickergallery.core.error.AppError

sealed class AppResult<out T> {
    data class Success<T>(
        val data: T,
    ) : AppResult<T>()

    data class Error(
        val error: AppError,
    ) : AppResult<Nothing>()
}
