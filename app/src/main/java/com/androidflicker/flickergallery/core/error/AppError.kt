package com.androidflicker.flickergallery.core.error

sealed class AppError {
    data class Network(
        val message: String = "No internet connection",
    ) : AppError()

    data class Timeout(
        val message: String = "Request timed out",
    ) : AppError()

    data class Unauthorized(
        val message: String = "Authentication required",
    ) : AppError()

    data class NotFound(
        val message: String = "Resource not found",
    ) : AppError()

    data class Server(
        val code: Int,
        val message: String? = null,
    ) : AppError()

    data class Serialization(
        val message: String = "Failed to parse response",
    ) : AppError()

    data class Unknown(
        val message: String = "Something went wrong",
    ) : AppError()
}

fun AppError.userMessage(): String =
    when (this) {
        is AppError.Network -> message
        is AppError.Timeout -> message
        is AppError.Unauthorized -> message
        is AppError.NotFound -> message
        is AppError.Server -> message ?: "Server error ($code)"
        is AppError.Serialization -> message
        is AppError.Unknown -> message
    }
