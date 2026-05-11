package com.androidflicker.flickergallery.data.remote.mapper

import com.androidflicker.flickergallery.data.remote.dto.FlickrSizeDto

object FlickrSizeSelector {
    private val DETAIL_PREFERRED_LABELS =
        listOf(
            "Large 1600",
            "Large 1024",
            "Large",
            "Medium 800",
            "Medium 640",
            "Medium",
        )

    fun selectForDetail(sizes: List<FlickrSizeDto>): FlickrSizeDto? {
        if (sizes.isEmpty()) return null
        DETAIL_PREFERRED_LABELS.forEach { label ->
            sizes.find { it.label == label }?.let { return it }
        }
        return sizes.maxByOrNull { it.width }
    }
}
