package com.androidflicker.flickergallery.core.error

sealed class AppError {
    data class Network(
        val message: String = "No internet connection",
    ) : AppError()

    data class Server(
        val code: Int,
        val message: String? = null,
    ) : AppError()

    data class Unknown(
        val message: String = "Something went wrong",
    ) : AppError()
}

fun AppError.userMessage(): String =
    when (this) {
        is AppError.Network -> message
        is AppError.Server -> message ?: "Server error ($code)"
        is AppError.Unknown -> message
    }
