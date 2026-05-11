package com.androidflicker.flickergallery.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

private val ProgressIndicatorSize = 20.dp
private val PreviewPosterWidth = 140.dp
private val PreviewPosterHeight = 210.dp
private const val PLACEHOLDER_ALPHA = 0.4f
private const val RETRY_ICON_ALPHA = 0.6f

@Composable
fun AppAsyncImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    var retryKey by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    SubcomposeAsyncImage(
        model =
            if (imageUrl.isBlank()) {
                null
            } else {
                ImageRequest
                    .Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .memoryCacheKey("${imageUrl}_$retryKey")
                    .diskCacheKey("${imageUrl}_$retryKey")
                    .build()
            },
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
    ) {
        when (val state = painter.state) {
            is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
            is AsyncImagePainter.State.Error -> AppImageError(onRetry = { retryKey++ })
            else -> AppImagePlaceholder(showProgress = state is AsyncImagePainter.State.Loading)
        }
    }
}

@Composable
internal fun AppImagePlaceholder(
    showProgress: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if (showProgress) {
            CircularProgressIndicator(
                modifier = Modifier.size(ProgressIndicatorSize),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = PLACEHOLDER_ALPHA),
            )
        }
    }
}

@Composable
internal fun AppImageError(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(
                    onClickLabel = "Retry loading image",
                    onClick = onRetry,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "↻",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = RETRY_ICON_ALPHA),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppAsyncImageLoadingPreview() {
    FlickerGalleryTheme {
        AppImagePlaceholder(
            showProgress = true,
            modifier = Modifier.size(PreviewPosterWidth, PreviewPosterHeight),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppAsyncImageEmptyPreview() {
    FlickerGalleryTheme {
        AppImagePlaceholder(
            showProgress = false,
            modifier = Modifier.size(PreviewPosterWidth, PreviewPosterHeight),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppAsyncImageErrorPreview() {
    FlickerGalleryTheme {
        AppImageError(
            onRetry = {},
            modifier = Modifier.size(PreviewPosterWidth, PreviewPosterHeight),
        )
    }
}
