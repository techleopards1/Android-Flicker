package com.androidflicker.flickergallery.core.error

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppErrorTest {
    @Test
    fun `Network error has default message`() {
        assertEquals("No internet connection", AppError.Network().message)
    }

    @Test
    fun `Network error accepts custom message`() {
        assertEquals("Timeout", AppError.Network("Timeout").message)
    }

    @Test
    fun `Timeout error has default message`() {
        assertEquals("Request timed out", AppError.Timeout().message)
    }

    @Test
    fun `Timeout error accepts custom message`() {
        assertEquals("Socket timeout", AppError.Timeout("Socket timeout").message)
    }

    @Test
    fun `Unauthorized error has default message`() {
        assertEquals("Authentication required", AppError.Unauthorized().message)
    }

    @Test
    fun `NotFound error has default message`() {
        assertEquals("Resource not found", AppError.NotFound().message)
    }

    @Test
    fun `Server error holds status code`() {
        assertEquals(500, AppError.Server(code = 500).code)
    }

    @Test
    fun `Serialization error has default message`() {
        assertEquals("Failed to parse response", AppError.Serialization().message)
    }

    @Test
    fun `Unknown error has default message`() {
        assertEquals("Something went wrong", AppError.Unknown().message)
    }

    @Test
    fun `userMessage returns Network message`() {
        assertEquals("No wifi", AppError.Network("No wifi").userMessage())
    }

    @Test
    fun `userMessage returns Timeout message`() {
        assertEquals("Request timed out", AppError.Timeout().userMessage())
    }

    @Test
    fun `userMessage returns Unauthorized message`() {
        assertEquals("Authentication required", AppError.Unauthorized().userMessage())
    }

    @Test
    fun `userMessage returns NotFound message`() {
        assertEquals("Resource not found", AppError.NotFound().userMessage())
    }

    @Test
    fun `userMessage returns custom Server message when present`() {
        assertEquals("Service unavailable", AppError.Server(503, "Service unavailable").userMessage())
    }

    @Test
    fun `userMessage falls back to code for Server without message`() {
        assertTrue(AppError.Server(500).userMessage().contains("500"))
    }

    @Test
    fun `userMessage returns Serialization message`() {
        assertEquals("Failed to parse response", AppError.Serialization().userMessage())
    }

    @Test
    fun `userMessage returns Unknown message`() {
        assertEquals("Crash", AppError.Unknown("Crash").userMessage())
    }
}
