package com.androidflicker.flickergallery.core.config

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EnvironmentConfigTest {
    @Test
    fun `apiBaseUrl is not empty`() {
        assertTrue(EnvironmentConfig.apiBaseUrl.isNotEmpty())
    }

    @Test
    fun `apiBaseUrl points to Flickr API in devDebug variant`() {
        assertEquals("https://www.flickr.com/", EnvironmentConfig.apiBaseUrl)
    }

    @Test
    fun `environment is not null`() {
        assertNotNull(EnvironmentConfig.environment)
    }

    @Test
    fun `environment is DEV in devDebug variant`() {
        assertEquals(Environment.DEV, EnvironmentConfig.environment)
    }

    @Test
    fun `enableLogging is true in devDebug variant`() {
        assertTrue(EnvironmentConfig.enableLogging)
    }

    @Test
    fun `isDebug is true in debug build`() {
        assertTrue(EnvironmentConfig.isDebug)
    }
}
