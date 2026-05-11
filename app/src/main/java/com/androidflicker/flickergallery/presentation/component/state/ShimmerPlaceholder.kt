package com.androidflicker.flickergallery.presentation.component.state

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

private val PosterWidth = 140.dp
private val PosterCornerRadius = 8.dp
private val TextCornerRadius = 4.dp
private val TitleHeight = 12.dp
private val SubtitleHeight = 10.dp
private val TitleTopSpacing = 6.dp
private val SubtitleTopSpacing = 4.dp
private val RowTitleWidth = 120.dp
private val RowTitleHeight = 20.dp
private val RowTitleBottomPadding = 10.dp
private val RowHorizontalPadding = 16.dp
private val RowItemSpacing = 12.dp
private const val SHIMMER_ITEM_COUNT = 5
private const val SHIMMER_ANIMATION_MS = 1_000
private const val SHIMMER_RANGE = 1_500f
private const val POSTER_ASPECT_RATIO = 2f / 3f
private const val TITLE_WIDTH_FRACTION = 0.8f
private const val SUBTITLE_WIDTH_FRACTION = 0.5f

@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = -SHIMMER_RANGE,
        targetValue = SHIMMER_RANGE,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = SHIMMER_ANIMATION_MS, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "shimmerOffset",
    )
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.surface
    return Brush.linearGradient(
        colors = listOf(base, highlight, base),
        start = Offset(progress, 0f),
        end = Offset(progress + SHIMMER_RANGE, 0f),
    )
}

@Composable
fun ShimmerPosterCard(
    shimmerBrush: Brush,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.width(PosterWidth)) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(POSTER_ASPECT_RATIO)
                    .clip(RoundedCornerShape(PosterCornerRadius))
                    .background(shimmerBrush),
        )
        Spacer(modifier = Modifier.height(TitleTopSpacing))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(TITLE_WIDTH_FRACTION)
                    .height(TitleHeight)
                    .clip(RoundedCornerShape(TextCornerRadius))
                    .background(shimmerBrush),
        )
        Spacer(modifier = Modifier.height(SubtitleTopSpacing))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(SUBTITLE_WIDTH_FRACTION)
                    .height(SubtitleHeight)
                    .clip(RoundedCornerShape(TextCornerRadius))
                    .background(shimmerBrush),
        )
    }
}

@Composable
fun ShimmerSwimlaneRow(
    shimmerBrush: Brush,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier =
                Modifier
                    .padding(start = RowHorizontalPadding, bottom = RowTitleBottomPadding)
                    .width(RowTitleWidth)
                    .height(RowTitleHeight)
                    .clip(RoundedCornerShape(TextCornerRadius))
                    .background(shimmerBrush),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = RowHorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(RowItemSpacing),
            userScrollEnabled = false,
        ) {
            items(SHIMMER_ITEM_COUNT) {
                ShimmerPosterCard(shimmerBrush = shimmerBrush)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerPosterCardPreview() {
    FlickerGalleryTheme {
        ShimmerPosterCard(
            shimmerBrush = rememberShimmerBrush(),
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerSwimlaneRowPreview() {
    FlickerGalleryTheme {
        ShimmerSwimlaneRow(shimmerBrush = rememberShimmerBrush())
    }
}
