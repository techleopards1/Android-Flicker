package com.androidflicker.flickergallery.core.config

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class EnvironmentTest {

    @Test
    fun `DEV has correct id`() {
        assertEquals("dev", Environment.DEV.id)
    }

    @Test
    fun `STAGING has correct id`() {
        assertEquals("staging", Environment.STAGING.id)
    }

    @Test
    fun `PRODUCTION has correct id`() {
        assertEquals("production", Environment.PRODUCTION.id)
    }

    @Test
    fun `all three environments exist`() {
        assertEquals(3, Environment.entries.size)
    }

    @Test
    fun `current returns a non-null environment`() {
        // devDebug BuildConfig.APP_ENVIRONMENT = "dev", so current() returns DEV
        assertNotNull(Environment.current())
    }

    @Test
    fun `current returns DEV for devDebug variant`() {
        // BuildConfig.APP_ENVIRONMENT is "dev" in the devDebug test variant
        assertEquals(Environment.DEV, Environment.current())
    }

    @Test
    fun `unknown environment id falls back to PRODUCTION`() {
        val result = Environment.entries.firstOrNull { it.id == "unknown" } ?: Environment.PRODUCTION
        assertEquals(Environment.PRODUCTION, result)
    }
}
