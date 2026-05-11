package com.androidflicker.flickergallery.core.network

import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkConstantsTest {
    @Test
    fun `timeout is 30 seconds`() {
        assertEquals(30L, NetworkConstants.TIMEOUT_SECONDS)
    }
}
