package com.androidflicker.flickergallery.presentation.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppDestinationTest {
    @Test
    fun `home route is correct`() {
        assertEquals("home", AppDestination.Home.route)
    }

    @Test
    fun `detail route template contains arg placeholder`() {
        assertEquals("detail/{photoId}", AppDestination.Detail.route)
    }

    @Test
    fun `detail ARG_PHOTO_ID constant matches route placeholder`() {
        assertTrue(AppDestination.Detail.route.contains(AppDestination.Detail.ARG_PHOTO_ID))
    }

    @Test
    fun `createRoute embeds photo id`() {
        val route = AppDestination.Detail.createRoute("12345")
        assertTrue(route.contains("12345"))
        assertTrue(route.startsWith("detail/"))
    }

    @Test
    fun `createRoute encodes spaces`() {
        val route = AppDestination.Detail.createRoute("id with spaces")
        assertFalse(' ' in route)
        assertTrue(route.contains("%20"))
    }

    @Test
    fun `createRoute encodes slash`() {
        val route = AppDestination.Detail.createRoute("id/with/slash")
        assertFalse(route.substringAfter("detail/").contains("/"))
    }

    @Test
    fun `deep link scheme is androidflicker`() {
        assertEquals("androidflicker", AppDeepLinks.SCHEME)
    }

    @Test
    fun `deep link host is photo`() {
        assertEquals("photo", AppDeepLinks.HOST)
    }

    @Test
    fun `deep link pattern starts with correct scheme and host`() {
        val pattern = AppDeepLinks.detailPattern()
        assertTrue(pattern.startsWith("androidflicker://photo/"))
    }

    @Test
    fun `deep link pattern contains photo id argument`() {
        val pattern = AppDeepLinks.detailPattern()
        assertTrue(pattern.contains(AppDestination.Detail.ARG_PHOTO_ID))
    }
}
