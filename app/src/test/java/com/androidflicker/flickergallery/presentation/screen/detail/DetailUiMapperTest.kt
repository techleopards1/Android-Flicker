package com.androidflicker.flickergallery.presentation.screen.detail

import com.androidflicker.flickergallery.domain.model.PhotoDetails
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class DetailUiMapperTest {
    private val details =
        PhotoDetails(
            id = "42",
            title = "Test Photo",
            description = "A great photo",
            imageUrl = "https://example.com/photo.jpg",
            owner = "nsid123",
            ownerName = "Jane Doe",
            views = 1234,
            dateUploaded = "1705276800",
            dateTaken = "2024-01-14 10:30:00",
            photoPageUrl = "https://www.flickr.com/photos/nsid123/42/",
        )

    @Test
    fun `maps all fields correctly`() {
        val model = details.toDetailUiModel()
        assertEquals("42", model.id)
        assertEquals("Test Photo", model.title)
        assertEquals("https://example.com/photo.jpg", model.imageUrl)
        assertEquals("A great photo", model.description)
        assertEquals("Jane Doe", model.authorName)
        assertEquals("https://www.flickr.com/photos/nsid123/42/", model.photoPageUrl)
    }

    @Test
    fun `blank description is replaced with fallback`() {
        val model = details.copy(description = "").toDetailUiModel()
        assertEquals("No description available.", model.description)
    }

    @Test
    fun `whitespace-only description is replaced with fallback`() {
        val model = details.copy(description = "   ").toDetailUiModel()
        assertEquals("No description available.", model.description)
    }

    @Test
    fun `views are formatted with number separator`() {
        val model = details.toDetailUiModel()
        assertFalse(model.views.isEmpty())
        // formatted number contains digits; 1234 → "1,234" or locale-specific
        assertEquals(1234, model.views.filter { it.isDigit() }.toInt())
    }

    @Test
    fun `unix timestamp dateUploaded is converted to readable date`() {
        val model = details.toDetailUiModel()
        // "1705276800" should not appear literally in output
        assertFalse(model.dateUploaded == "1705276800")
        assertFalse(model.dateUploaded.isBlank())
    }

    @Test
    fun `dateTaken strips time component`() {
        val model = details.toDetailUiModel()
        assertEquals("2024-01-14", model.dateTaken)
    }
}
