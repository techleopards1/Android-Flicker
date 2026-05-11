package com.androidflicker.flickergallery.data.remote.mapper

import com.androidflicker.flickergallery.data.remote.dto.PhotoDetailsDto
import com.androidflicker.flickergallery.data.remote.dto.PhotoDto
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.domain.model.PhotoDetails

fun PhotoDto.toDomain(): Photo =
    Photo(
        id = id,
        title = title.ifEmpty { "Untitled" },
        imageUrl = "https://live.staticflickr.com/$server/${id}_$secret.jpg",
        owner = owner,
    )

fun List<PhotoDto>.toDomain(): List<Photo> = map { it.toDomain() }

fun PhotoDetailsDto.toDomain(): PhotoDetails =
    PhotoDetails(
        id = id,
        title = title.content.ifEmpty { "Untitled" },
        description = description.content,
        imageUrl = "https://live.staticflickr.com/$server/${id}_$secret.jpg",
        owner = owner.nsid,
        ownerName = owner.realname.ifEmpty { owner.username },
        views = views.toIntOrNull() ?: 0,
        dateUploaded = dates.posted,
        dateTaken = dates.taken,
        photoPageUrl = "https://www.flickr.com/photos/${owner.nsid}/$id/",
    )
