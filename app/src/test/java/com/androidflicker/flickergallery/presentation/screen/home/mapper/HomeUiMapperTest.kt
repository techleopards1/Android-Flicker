package com.androidflicker.flickergallery.presentation.screen.home.mapper

import com.androidflicker.flickergallery.core.error.AppError
import com.androidflicker.flickergallery.core.result.AppResult
import com.androidflicker.flickergallery.domain.model.HomeCategory
import com.androidflicker.flickergallery.domain.model.Photo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeUiMapperTest {
    private val photo = Photo(id = "42", title = "Test Title", imageUrl = "http://img", owner = "Test Owner")

    @Test
    fun `photo maps to HomeItemUiModel with correct fields`() {
        val model = photo.toHomeItemUiModel()
        assertEquals("42", model.id)
        assertEquals("Test Title", model.title)
        assertEquals("http://img", model.imageUrl)
        assertEquals("Test Owner", model.subtitle)
    }

    @Test
    fun `success result maps to list of ui models`() {
        val result: AppResult<List<Photo>> = AppResult.Success(listOf(photo))
        val models = result.toHomeItemUiModels()
        assertEquals(1, models.size)
        assertEquals("42", models.first().id)
    }

    @Test
    fun `error result maps to empty list`() {
        val result: AppResult<List<Photo>> = AppResult.Error(AppError.Network())
        assertTrue(result.toHomeItemUiModels().isEmpty())
    }

    @Test
    fun `empty success result maps to empty list`() {
        val result: AppResult<List<Photo>> = AppResult.Success(emptyList())
        assertTrue(result.toHomeItemUiModels().isEmpty())
    }

    @Test
    fun `success pair maps to category ui model with items and no error`() {
        val pair = HomeCategory.NATURE to AppResult.Success(listOf(photo)) as AppResult<List<Photo>>
        val model = pair.toCategoryUiModel()
        assertEquals(HomeCategory.NATURE.id, model.id)
        assertEquals(HomeCategory.NATURE.displayTitle, model.title)
        assertEquals(1, model.items.size)
        assertNull(model.errorMessage)
        assertFalse(model.isEmpty)
    }

    @Test
    fun `success with empty list maps to isEmpty true`() {
        val pair = HomeCategory.NATURE to AppResult.Success(emptyList<Photo>()) as AppResult<List<Photo>>
        val model = pair.toCategoryUiModel()
        assertTrue(model.isEmpty)
        assertTrue(model.items.isEmpty())
        assertNull(model.errorMessage)
    }

    @Test
    fun `error pair maps to isEmpty false and sets error message`() {
        val pair = HomeCategory.SPACE to AppResult.Error(AppError.Network()) as AppResult<List<Photo>>
        val model = pair.toCategoryUiModel()
        assertEquals(HomeCategory.SPACE.id, model.id)
        assertTrue(model.items.isEmpty())
        assertFalse(model.isEmpty)
        assertNotNull(model.errorMessage)
    }
}
