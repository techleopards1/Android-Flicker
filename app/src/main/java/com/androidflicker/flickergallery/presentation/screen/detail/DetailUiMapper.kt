package com.androidflicker.flickergallery.presentation.screen.detail

import com.androidflicker.flickergallery.domain.model.PhotoDetails
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun PhotoDetails.toDetailUiModel() =
    DetailUiModel(
        id = id,
        title = title,
        imageUrl = imageUrl,
        description = description.ifBlank { "No description available." },
        authorName = ownerName,
        views = views.toFormattedViews(),
        dateUploaded = dateUploaded.toFormattedDate(),
        dateTaken = dateTaken.substringBefore(" "),
        photoPageUrl = photoPageUrl,
    )

private fun Int.toFormattedViews(): String = NumberFormat.getNumberInstance(Locale.getDefault()).format(this)

private const val MILLIS_PER_SECOND = 1_000L

private fun String.toFormattedDate(): String =
    toLongOrNull()?.let {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(it * MILLIS_PER_SECOND))
    } ?: this
