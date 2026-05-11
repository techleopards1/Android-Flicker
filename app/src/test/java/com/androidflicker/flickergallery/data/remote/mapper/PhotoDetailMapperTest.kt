package com.androidflicker.flickergallery.data.remote.mapper

import com.androidflicker.flickergallery.data.remote.dto.PhotoDatesDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoDetailsDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoOwnerDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoTextDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PhotoDetailMapperTest {
    private val sampleDto =
        PhotoDetailsDto(
            id = "99999",
            secret = "secret1",
            server = "65535",
            title = PhotoTextDto("Autumn Leaves"),
            description = PhotoTextDto("A photo of autumn"),
            owner = PhotoOwnerDto(nsid = "owner@flickr", username = "user1", realname = "Real User"),
            dates = PhotoDatesDto(posted = "1700000000", taken = "2023-11-01 10:00:00"),
            views = "142",
        )

    @Test
    fun `toDomain maps id correctly`() {
        assertEquals("99999", sampleDto.toDomain().id)
    }

    @Test
    fun `toDomain maps title from nested content`() {
        assertEquals("Autumn Leaves", sampleDto.toDomain().title)
    }

    @Test
    fun `toDomain maps description from nested content`() {
        assertEquals("A photo of autumn", sampleDto.toDomain().description)
    }

    @Test
    fun `toDomain builds correct image URL`() {
        val url = sampleDto.toDomain().imageUrl
        assertTrue(url.contains("65535"))
        assertTrue(url.contains("99999"))
        assertTrue(url.contains("secret1"))
        assertTrue(url.startsWith("https://live.staticflickr.com/"))
    }

    @Test
    fun `toDomain uses realname for ownerName when present`() {
        assertEquals("Real User", sampleDto.toDomain().ownerName)
    }

    @Test
    fun `toDomain falls back to username when realname is empty`() {
        val dto = sampleDto.copy(owner = sampleDto.owner.copy(realname = ""))
        assertEquals("user1", dto.toDomain().ownerName)
    }

    @Test
    fun `toDomain parses views as integer`() {
        assertEquals(142, sampleDto.toDomain().views)
    }

    @Test
    fun `toDomain defaults views to zero for non-numeric value`() {
        val dto = sampleDto.copy(views = "N/A")
        assertEquals(0, dto.toDomain().views)
    }

    @Test
    fun `toDomain replaces empty title with Untitled`() {
        val dto = sampleDto.copy(title = PhotoTextDto(""))
        assertEquals("Untitled", dto.toDomain().title)
    }

    @Test
    fun `toDomain builds correct photo page URL`() {
        val url = sampleDto.toDomain().photoPageUrl
        assertTrue(url.contains("owner@flickr"))
        assertTrue(url.contains("99999"))
        assertTrue(url.startsWith("https://www.flickr.com/photos/"))
    }

    @Test
    fun `toDomain maps uploaded date`() {
        assertEquals("1700000000", sampleDto.toDomain().dateUploaded)
    }

    @Test
    fun `toDomain maps taken date`() {
        assertEquals("2023-11-01 10:00:00", sampleDto.toDomain().dateTaken)
    }
}
