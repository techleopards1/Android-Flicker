package com.androidflicker.flickergallery.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.androidflicker.flickergallery.presentation.screen.home.HomeItemUiModel
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

private val PosterCardWidth = 140.dp
private val PosterCornerRadius = 8.dp
private val PosterTitlePadding = 6.dp
private const val POSTER_ASPECT_RATIO = 2f / 3f

@Composable
fun PosterCard(
    item: HomeItemUiModel,
    onItemClick: (HomeItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.width(PosterCardWidth)) {
        Card(
            modifier =
                Modifier
                    .fillMaxSize()
                    .aspectRatio(POSTER_ASPECT_RATIO)
                    .clip(RoundedCornerShape(PosterCornerRadius))
                    .clickable { onItemClick(item) },
            shape = RoundedCornerShape(PosterCornerRadius),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                AsyncImage(
                    model = item.imageUrl.ifEmpty { null },
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Text(
            text = item.title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = PosterTitlePadding),
        )

        item.subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PosterCardPreview() {
    FlickerGalleryTheme {
        PosterCard(
            item =
                HomeItemUiModel(
                    id = "1",
                    title = "Golden Gate at Sunset",
                    imageUrl = "",
                    subtitle = "by photographer",
                ),
            onItemClick = {},
            modifier = Modifier.padding(8.dp),
        )
    }
}
