package com.androidflicker.flickergallery.core.network

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

private const val HTTP_UNAUTHORIZED = 401
private const val HTTP_NOT_FOUND = 404
private const val HTTP_SERVER_ERROR_MIN = 500
private const val HTTP_SERVER_ERROR_MAX = 599

@Suppress("TooGenericExceptionCaught")
suspend fun <T> safeApiCall(block: suspend () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (e: HttpException) {
        when (e.code()) {
            HTTP_UNAUTHORIZED -> AppResult.Error(AppError.Unauthorized())
            HTTP_NOT_FOUND -> AppResult.Error(AppError.NotFound())
            in HTTP_SERVER_ERROR_MIN..HTTP_SERVER_ERROR_MAX ->
                AppResult.Error(AppError.Server(e.code(), e.message()))
            else -> AppResult.Error(AppError.Unknown(e.message()))
        }
    } catch (e: SocketTimeoutException) {
        AppResult.Error(AppError.Timeout(e.message ?: "Request timed out"))
    } catch (e: IOException) {
        AppResult.Error(AppError.Network(e.message ?: "Network failure"))
    } catch (e: JsonSyntaxException) {
        AppResult.Error(AppError.Serialization(e.message ?: "Failed to parse response"))
    } catch (e: Exception) {
        AppResult.Error(AppError.Unknown(e.message ?: "Unexpected error"))
    }
