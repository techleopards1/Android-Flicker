package com.androidflicker.flickergallery.data.remote.mapper

import com.androidflicker.flickergallery.data.remote.dto.PhotoDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PhotoMapperTest {
    private val sampleDto =
        PhotoDto(
            id = "12345",
            owner = "user@flickr",
            secret = "abc123",
            server = "65535",
            farm = 66,
            title = "Golden Gate",
        )

    @Test
    fun `toDomain maps id correctly`() {
        assertEquals("12345", sampleDto.toDomain().id)
    }

    @Test
    fun `toDomain maps title correctly`() {
        assertEquals("Golden Gate", sampleDto.toDomain().title)
    }

    @Test
    fun `toDomain maps owner correctly`() {
        assertEquals("user@flickr", sampleDto.toDomain().owner)
    }

    @Test
    fun `toDomain builds correct Flickr image URL`() {
        val url = sampleDto.toDomain().imageUrl
        assertTrue(url.contains("65535"))
        assertTrue(url.contains("12345"))
        assertTrue(url.contains("abc123"))
        assertTrue(url.startsWith("https://live.staticflickr.com/"))
    }

    @Test
    fun `toDomain replaces empty title with Untitled`() {
        val dto = sampleDto.copy(title = "")
        assertEquals("Untitled", dto.toDomain().title)
    }

    @Test
    fun `list toDomain maps all items`() {
        val dtos = listOf(sampleDto, sampleDto.copy(id = "99999"))
        val photos = dtos.toDomain()
        assertEquals(2, photos.size)
        assertEquals("12345", photos[0].id)
        assertEquals("99999", photos[1].id)
    }

    @Test
    fun `list toDomain returns empty list for empty input`() {
        assertTrue(emptyList<PhotoDto>().toDomain().isEmpty())
    }
}
