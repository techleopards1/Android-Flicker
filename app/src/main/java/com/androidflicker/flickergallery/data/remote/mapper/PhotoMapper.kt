package com.androidflicker.flickergallery.data.remote.mapper

import com.androidflicker.flickergallery.data.remote.dto.PhotoDto
import com.androidflicker.flickergallery.domain.model.Photo

fun PhotoDto.toDomain(): Photo =
    Photo(
        id = id,
        title = title.ifEmpty { "Untitled" },
        imageUrl = "https://live.staticflickr.com/$server/${id}_$secret.jpg",
        owner = owner,
    )

fun List<PhotoDto>.toDomain(): List<Photo> = map { it.toDomain() }
