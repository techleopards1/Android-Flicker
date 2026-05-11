package com.androidflicker.flickergallery.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidflicker.flickergallery.presentation.screen.home.HomeItemUiModel
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

private val SectionTitleBottomPadding = 10.dp
private val SectionHorizontalPadding = 16.dp
private val SectionItemSpacing = 12.dp
private val SectionStatusVerticalPadding = 8.dp
private const val PREVIEW_ITEM_COUNT = 5

@Composable
fun SwimlaneSection(
    title: String,
    items: List<HomeItemUiModel>,
    onItemClick: (HomeItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier =
                Modifier.padding(
                    start = SectionHorizontalPadding,
                    bottom = SectionTitleBottomPadding,
                ),
        )
        when {
            errorMessage != null ->
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier =
                        Modifier.padding(
                            horizontal = SectionHorizontalPadding,
                            vertical = SectionStatusVerticalPadding,
                        ),
                )
            items.isEmpty() ->
                Text(
                    text = "No items available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier =
                        Modifier.padding(
                            horizontal = SectionHorizontalPadding,
                            vertical = SectionStatusVerticalPadding,
                        ),
                )
            else ->
                LazyRow(
                    contentPadding = PaddingValues(horizontal = SectionHorizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(SectionItemSpacing),
                ) {
                    items(items = items, key = { it.id }) { item ->
                        PosterCard(
                            item = item,
                            onItemClick = onItemClick,
                        )
                    }
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SwimlaneSectionPreview() {
    FlickerGalleryTheme {
        SwimlaneSection(
            title = "Popular",
            items =
                List(PREVIEW_ITEM_COUNT) { index ->
                    HomeItemUiModel(
                        id = "$index",
                        title = "Photo ${index + 1}",
                        imageUrl = "",
                        subtitle = "owner",
                    )
                },
            onItemClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwimlaneSectionEmptyPreview() {
    FlickerGalleryTheme {
        SwimlaneSection(
            title = "Space",
            items = emptyList(),
            onItemClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwimlaneSectionErrorPreview() {
    FlickerGalleryTheme {
        SwimlaneSection(
            title = "Travel",
            items = emptyList(),
            onItemClick = {},
            errorMessage = "No internet connection",
        )
    }
}
