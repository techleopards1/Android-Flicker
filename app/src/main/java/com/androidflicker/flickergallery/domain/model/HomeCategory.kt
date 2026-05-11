package com.androidflicker.flickergallery.domain.model

enum class HomeCategory(
    val id: String,
    val displayTitle: String,
) {
    NATURE("nature", "Nature"),
    ANIMALS("animals", "Animals"),
    ARCHITECTURE("architecture", "Architecture"),
    HISTORICAL("historical", "Historical"),
    POPULAR("popular", "Popular"),
    TRENDING("trending", "Trending"),
    TECHNOLOGY("technology", "Technology"),
    SPACE("space", "Space"),
    TRAVEL("travel", "Travel"),
    RECENT("recent", "Recent"),
}
