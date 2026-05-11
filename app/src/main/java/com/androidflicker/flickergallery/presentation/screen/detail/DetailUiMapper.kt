package com.androidflicker.flickergallery.presentation.screen.detail

import com.androidflicker.flickergallery.core.util.HtmlSanitizer
import com.androidflicker.flickergallery.domain.model.PhotoDetails
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MILLIS_PER_SECOND = 1_000L
private const val MIN_ASPECT_RATIO = 0.75f
private const val MAX_ASPECT_RATIO = 1.8f

fun PhotoDetails.toDetailUiModel() =
    DetailUiModel(
        id = id,
        title = title,
        imageUrl = imageUrl,
        description = HtmlSanitizer.sanitize(description),
        authorName = ownerName,
        views = views.toFormattedViews(),
        dateUploaded = dateUploaded.toFormattedDate(),
        dateTaken = dateTaken.substringBefore(" "),
        photoPageUrl = photoPageUrl,
        dimensionsLabel = if (width != null && height != null) "$width × $height" else null,
        sizeLabel = sizeLabel,
        imageAspectRatio = computeAspectRatio(width, height),
        tags = tags,
    )

private fun computeAspectRatio(
    width: Int?,
    height: Int?,
): Float {
    val validDimensions = width != null && height != null && width > 0 && height > 0
    if (!validDimensions) return DEFAULT_ASPECT_RATIO
    return (width!!.toFloat() / height!!.toFloat()).coerceIn(MIN_ASPECT_RATIO, MAX_ASPECT_RATIO)
}

private fun Int.toFormattedViews(): String = NumberFormat.getNumberInstance(Locale.getDefault()).format(this)

private fun String.toFormattedDate(): String =
    toLongOrNull()?.let {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(it * MILLIS_PER_SECOND))
    } ?: this
