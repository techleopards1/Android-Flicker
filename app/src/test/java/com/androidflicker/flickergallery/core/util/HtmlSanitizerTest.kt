package com.androidflicker.flickergallery.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class HtmlSanitizerTest {
    @Test
    fun `null returns fallback`() {
        assertEquals(HtmlSanitizer.FALLBACK, HtmlSanitizer.sanitize(null))
    }

    @Test
    fun `blank string returns fallback`() {
        assertEquals(HtmlSanitizer.FALLBACK, HtmlSanitizer.sanitize(""))
    }

    @Test
    fun `whitespace-only returns fallback`() {
        assertEquals(HtmlSanitizer.FALLBACK, HtmlSanitizer.sanitize("   \n  "))
    }

    @Test
    fun `plain text is returned unchanged`() {
        assertEquals("Hello world", HtmlSanitizer.sanitize("Hello world"))
    }

    @Test
    fun `br tags are stripped`() {
        val result = HtmlSanitizer.sanitize("Line one<br />Line two")
        assert("br" !in result) { "Expected no br tags, got: $result" }
    }

    @Test
    fun `anchor tags are stripped but text preserved`() {
        val result = HtmlSanitizer.sanitize("<a href=\"http://example.com\">click here</a>")
        assertEquals("click here", result)
    }

    @Test
    fun `amp entity is decoded`() {
        assertEquals("cats & dogs", HtmlSanitizer.sanitize("cats &amp; dogs"))
    }

    @Test
    fun `lt and gt entities are decoded`() {
        assertEquals("a < b > c", HtmlSanitizer.sanitize("a &lt; b &gt; c"))
    }

    @Test
    fun `nbsp entity becomes space`() {
        val result = HtmlSanitizer.sanitize("hello&nbsp;world")
        assertEquals("hello world", result)
    }

    @Test
    fun `multiple spaces are collapsed`() {
        assertEquals("a b c", HtmlSanitizer.sanitize("a   b   c"))
    }

    @Test
    fun `html-only content returns fallback`() {
        assertEquals(HtmlSanitizer.FALLBACK, HtmlSanitizer.sanitize("<br /><br />"))
    }

    @Test
    fun `mixed html and text works correctly`() {
        val result = HtmlSanitizer.sanitize("<p>Great <b>photo</b> of &amp; nature!</p>")
        assertEquals("Great photo of & nature!", result)
    }
}
