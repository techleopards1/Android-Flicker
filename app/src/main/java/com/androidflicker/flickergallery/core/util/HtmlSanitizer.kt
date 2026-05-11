package com.androidflicker.flickergallery.core.util

object HtmlSanitizer {
    private val HTML_TAG_REGEX = Regex("<[^>]*>")
    private val MULTI_WHITESPACE_REGEX = Regex("\\s{2,}")

    private val HTML_ENTITIES =
        mapOf(
            "&amp;" to "&",
            "&lt;" to "<",
            "&gt;" to ">",
            "&quot;" to "\"",
            "&#39;" to "'",
            "&apos;" to "'",
            "&nbsp;" to " ",
        )

    const val FALLBACK = "No description available."

    fun sanitize(raw: String?): String {
        if (raw.isNullOrBlank()) return FALLBACK
        var result = raw.replace(HTML_TAG_REGEX, " ")
        HTML_ENTITIES.forEach { (entity, replacement) ->
            result = result.replace(entity, replacement)
        }
        return result
            .replace(MULTI_WHITESPACE_REGEX, " ")
            .trim()
            .ifBlank { FALLBACK }
    }
}
