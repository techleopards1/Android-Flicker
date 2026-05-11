package com.androidflicker.flickergallery.data.remote.mapper

import com.androidflicker.flickergallery.data.remote.dto.FlickrSizeDto
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

fun PhotoDetailsDto.toDomain(selectedSize: FlickrSizeDto? = null): PhotoDetails {
    val imageUrl =
        selectedSize?.source
            ?: "https://live.staticflickr.com/$server/${id}_$secret.jpg"
    return PhotoDetails(
        id = id,
        title = title.content.ifEmpty { "Untitled" },
        description = description.content,
        imageUrl = imageUrl,
        owner = owner.nsid,
        ownerName = owner.realname.ifEmpty { owner.username },
        views = views.toIntOrNull() ?: 0,
        dateUploaded = dates.posted,
        dateTaken = dates.taken,
        photoPageUrl = "https://www.flickr.com/photos/${owner.nsid}/$id/",
        width = selectedSize?.width,
        height = selectedSize?.height,
        sizeLabel = selectedSize?.label,
        tags = tags?.tag?.map { it.content }?.filter { it.isNotBlank() } ?: emptyList(),
    )
}
