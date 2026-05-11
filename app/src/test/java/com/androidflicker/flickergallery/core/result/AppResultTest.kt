package com.androidflicker.flickergallery.core.result

import com.androidflicker.flickergallery.core.error.AppError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppResultTest {
    @Test
    fun `Success holds correct data`() {
        val result = AppResult.Success("test")
        assertTrue(result is AppResult.Success)
        assertEquals("test", result.data)
    }

    @Test
    fun `Success with list holds correct data`() {
        val result = AppResult.Success(listOf(1, 2, 3))
        assertEquals(3, result.data.size)
    }

    @Test
    fun `Error holds correct AppError`() {
        val error = AppError.Network()
        val result = AppResult.Error(error)
        assertTrue(result is AppResult.Error)
        assertEquals(error, result.error)
    }

    @Test
    fun `Error with Server error holds correct code`() {
        val error = AppError.Server(code = 500)
        val result = AppResult.Error(error)
        assertEquals(500, (result.error as AppError.Server).code)
    }
}
