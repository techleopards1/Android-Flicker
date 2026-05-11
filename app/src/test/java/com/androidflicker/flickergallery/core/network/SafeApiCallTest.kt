package com.androidflicker.flickergallery.core.network

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class SafeApiCallTest {
    @Test
    fun `success returns AppResult Success`() =
        runBlocking {
            val result = safeApiCall { "data" }
            assertTrue(result is AppResult.Success)
            assertEquals("data", (result as AppResult.Success).data)
        }

    @Test
    fun `IOException returns Network error`() =
        runBlocking {
            val result = safeApiCall { throw IOException("connection refused") }
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Network)
        }

    @Test
    fun `SocketTimeoutException returns Timeout error`() =
        runBlocking {
            val result = safeApiCall { throw SocketTimeoutException("timed out") }
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Timeout)
        }

    @Test
    fun `JsonSyntaxException returns Serialization error`() =
        runBlocking {
            val result = safeApiCall { throw JsonSyntaxException("malformed json") }
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Serialization)
        }

    @Test
    fun `HTTP 401 returns Unauthorized error`() =
        runBlocking {
            val body = "".toResponseBody("application/json".toMediaType())
            val result = safeApiCall { throw HttpException(Response.error<Any>(401, body)) }
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Unauthorized)
        }

    @Test
    fun `HTTP 404 returns NotFound error`() =
        runBlocking {
            val body = "".toResponseBody("application/json".toMediaType())
            val result = safeApiCall { throw HttpException(Response.error<Any>(404, body)) }
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.NotFound)
        }

    @Test
    fun `HTTP 500 returns Server error`() =
        runBlocking {
            val body = "".toResponseBody("application/json".toMediaType())
            val result = safeApiCall { throw HttpException(Response.error<Any>(500, body)) }
            assertTrue(result is AppResult.Error)
            val error = (result as AppResult.Error).error
            assertTrue(error is AppError.Server)
            assertEquals(500, (error as AppError.Server).code)
        }

    @Test
    fun `unexpected Exception returns Unknown error`() =
        runBlocking {
            val result = safeApiCall { throw IllegalStateException("unexpected") }
            assertTrue(result is AppResult.Error)
            assertTrue((result as AppResult.Error).error is AppError.Unknown)
        }
}
