package com.androidflicker.flickergallery.domain.model

import org.junit.Assert.assertTrue
import org.junit.Test

class HomeCategoryTest {
    @Test
    fun `at least 10 categories are defined`() {
        assertTrue(HomeCategory.entries.size >= 10)
    }

    @Test
    fun `all required categories are present`() {
        val ids = HomeCategory.entries.map { it.id }.toSet()
        val required =
            setOf(
                "nature",
                "animals",
                "architecture",
                "historical",
                "popular",
                "trending",
                "technology",
                "space",
                "travel",
                "recent",
            )
        assertTrue(ids.containsAll(required))
    }

    @Test
    fun `every category has a non-blank id`() {
        HomeCategory.entries.forEach { category ->
            assertTrue("${category.name} id is blank", category.id.isNotBlank())
        }
    }

    @Test
    fun `every category has a non-blank display title`() {
        HomeCategory.entries.forEach { category ->
            assertTrue("${category.name} displayTitle is blank", category.displayTitle.isNotBlank())
        }
    }

    @Test
    fun `category ids are unique`() {
        val ids = HomeCategory.entries.map { it.id }
        assertTrue(ids.size == ids.distinct().size)
    }
}
