package com.androidflicker.flickergallery.core.error

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppErrorTest {
    @Test
    fun `Network error has default message`() {
        val error = AppError.Network()
        assertEquals("No internet connection", error.message)
    }

    @Test
    fun `Network error accepts custom message`() {
        val error = AppError.Network("Timeout")
        assertEquals("Timeout", error.message)
    }

    @Test
    fun `Server error holds status code`() {
        val error = AppError.Server(code = 404)
        assertEquals(404, error.code)
    }

    @Test
    fun `Unknown error has default message`() {
        val error = AppError.Unknown()
        assertEquals("Something went wrong", error.message)
    }

    @Test
    fun `userMessage returns Network message`() {
        val msg = AppError.Network("No wifi").userMessage()
        assertEquals("No wifi", msg)
    }

    @Test
    fun `userMessage returns custom Server message when present`() {
        val msg = AppError.Server(code = 503, message = "Service unavailable").userMessage()
        assertEquals("Service unavailable", msg)
    }

    @Test
    fun `userMessage falls back to code for Server without message`() {
        val msg = AppError.Server(code = 500).userMessage()
        assertTrue(msg.contains("500"))
    }

    @Test
    fun `userMessage returns Unknown message`() {
        val msg = AppError.Unknown("Crash").userMessage()
        assertEquals("Crash", msg)
    }
}
