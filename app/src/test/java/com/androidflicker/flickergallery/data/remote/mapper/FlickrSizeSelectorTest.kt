package com.androidflicker.flickergallery.data.remote.mapper

import com.androidflicker.flickergallery.data.remote.dto.FlickrSizeDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FlickrSizeSelectorTest {
    private fun size(
        label: String,
        width: Int,
        height: Int = 100,
    ) = FlickrSizeDto(label = label, width = width, height = height, source = "url", media = "photo")

    @Test
    fun `empty list returns null`() {
        assertNull(FlickrSizeSelector.selectForDetail(emptyList()))
    }

    @Test
    fun `prefers Large 1600 over all others`() {
        val sizes =
            listOf(
                size("Original", 4000),
                size("Large 1600", 1600),
                size("Large", 1024),
                size("Medium 800", 800),
            )
        assertEquals("Large 1600", FlickrSizeSelector.selectForDetail(sizes)?.label)
    }

    @Test
    fun `falls back to Large when Large 1600 absent`() {
        val sizes = listOf(size("Original", 4000), size("Large", 1024), size("Medium 800", 800))
        assertEquals("Large", FlickrSizeSelector.selectForDetail(sizes)?.label)
    }

    @Test
    fun `falls back to Medium 800 when larger sizes absent`() {
        val sizes = listOf(size("Small", 240), size("Medium 800", 800), size("Medium 640", 640))
        assertEquals("Medium 800", FlickrSizeSelector.selectForDetail(sizes)?.label)
    }

    @Test
    fun `falls back to largest available when no preferred label matches`() {
        val sizes = listOf(size("Small", 240), size("Thumbnail", 100))
        assertEquals("Small", FlickrSizeSelector.selectForDetail(sizes)?.label)
    }

    @Test
    fun `returns correct source url`() {
        val sizes = listOf(size("Large 1600", 1600).copy(source = "https://cdn.example.com/large.jpg"))
        assertEquals("https://cdn.example.com/large.jpg", FlickrSizeSelector.selectForDetail(sizes)?.source)
    }
}
