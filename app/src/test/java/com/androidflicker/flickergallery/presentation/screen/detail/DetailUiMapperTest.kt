package com.androidflicker.flickergallery.presentation.screen.detail

import com.androidflicker.flickergallery.core.util.HtmlSanitizer
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
    fun `maps all base fields correctly`() {
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
        assertEquals(HtmlSanitizer.FALLBACK, model.description)
    }

    @Test
    fun `whitespace-only description is replaced with fallback`() {
        val model = details.copy(description = "   ").toDetailUiModel()
        assertEquals(HtmlSanitizer.FALLBACK, model.description)
    }

    @Test
    fun `html description is stripped`() {
        val model = details.copy(description = "<b>Bold</b> text &amp; more").toDetailUiModel()
        assertEquals("Bold text & more", model.description)
    }

    @Test
    fun `views are formatted with number separator`() {
        val model = details.toDetailUiModel()
        assertFalse(model.views.isEmpty())
        assertEquals(1234, model.views.filter { it.isDigit() }.toInt())
    }

    @Test
    fun `unix timestamp dateUploaded is converted to readable date`() {
        val model = details.toDetailUiModel()
        assertFalse(model.dateUploaded == "1705276800")
        assertFalse(model.dateUploaded.isBlank())
    }

    @Test
    fun `dateTaken strips time component`() {
        val model = details.toDetailUiModel()
        assertEquals("2024-01-14", model.dateTaken)
    }

    @Test
    fun `dimensionsLabel is null when no width and height`() {
        val model = details.copy(width = null, height = null).toDetailUiModel()
        assertNull(model.dimensionsLabel)
    }

    @Test
    fun `dimensionsLabel formats correctly when width and height present`() {
        val model = details.copy(width = 1600, height = 1067).toDetailUiModel()
        assertEquals("1600 × 1067", model.dimensionsLabel)
    }

    @Test
    fun `imageAspectRatio defaults to 16 9 when no dimensions`() {
        val model = details.copy(width = null, height = null).toDetailUiModel()
        assertEquals(DEFAULT_ASPECT_RATIO, model.imageAspectRatio)
    }

    @Test
    fun `imageAspectRatio is computed from width and height`() {
        // 1600 / 1067 ≈ 1.5, within the [0.75, 1.8] clamp range
        val model = details.copy(width = 1600, height = 1067).toDetailUiModel()
        assertEquals(1600f / 1067f, model.imageAspectRatio, 0.01f)
    }

    @Test
    fun `imageAspectRatio is clamped to max 1 8`() {
        val model = details.copy(width = 4000, height = 100).toDetailUiModel()
        assertEquals(1.8f, model.imageAspectRatio, 0.01f)
    }

    @Test
    fun `imageAspectRatio is clamped to min 0 75`() {
        val model = details.copy(width = 100, height = 4000).toDetailUiModel()
        assertEquals(0.75f, model.imageAspectRatio, 0.01f)
    }

    @Test
    fun `sizeLabel is null when not present`() {
        assertNull(details.toDetailUiModel().sizeLabel)
    }

    @Test
    fun `sizeLabel is passed through when present`() {
        val model = details.copy(sizeLabel = "Large 1600").toDetailUiModel()
        assertEquals("Large 1600", model.sizeLabel)
    }

    @Test
    fun `tags are passed through`() {
        val model = details.copy(tags = listOf("nature", "landscape")).toDetailUiModel()
        assertEquals(listOf("nature", "landscape"), model.tags)
    }

    @Test
    fun `empty tags list maps to empty list`() {
        assertTrue(
            details
                .copy(tags = emptyList())
                .toDetailUiModel()
                .tags
                .isEmpty(),
        )
    }
}
