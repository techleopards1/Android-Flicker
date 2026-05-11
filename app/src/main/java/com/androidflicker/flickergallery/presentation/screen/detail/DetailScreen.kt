package com.androidflicker.flickergallery.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androidflicker.flickergallery.presentation.component.AppAsyncImage
import com.androidflicker.flickergallery.presentation.component.state.EmptyState
import com.androidflicker.flickergallery.presentation.component.state.ErrorState
import com.androidflicker.flickergallery.presentation.component.state.LoadingState
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

private val ContentPadding = 16.dp
private val SectionSpacing = 20.dp
private val MetaRowSpacing = 8.dp
private val TagCornerRadius = 4.dp
private val TagHorizontalPadding = 8.dp
private val TagVerticalPadding = 4.dp
private val TagRowSpacing = 6.dp

@Composable
fun DetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    uiState: DetailUiState,
    onEvent: (DetailEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.item?.title ?: "Details",
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("← Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            uiState.isLoading ->
                LoadingState(modifier = Modifier.padding(innerPadding))
            uiState.errorMessage != null ->
                ErrorState(
                    message = uiState.errorMessage,
                    onRetry = { onEvent(DetailEvent.Retry) },
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            uiState.isEmpty || uiState.item == null ->
                EmptyState(
                    title = "Photo not found",
                    description = "This photo may have been removed or is unavailable.",
                    actionLabel = "Go back",
                    onActionClick = onNavigateBack,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )
            else ->
                DetailBody(
                    item = uiState.item,
                    modifier = Modifier.padding(innerPadding),
                )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailBody(
    item: DetailUiModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            AppAsyncImage(
                imageUrl = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(item.imageAspectRatio),
            )
        }
        item {
            Column(modifier = Modifier.padding(ContentPadding)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "by ${item.authorName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                )
            }
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = ContentPadding))
            Column(
                modifier = Modifier.padding(ContentPadding),
                verticalArrangement = Arrangement.spacedBy(MetaRowSpacing),
            ) {
                MetadataRow(label = "Views", value = item.views)
                MetadataRow(label = "Uploaded", value = item.dateUploaded)
                if (item.dateTaken.isNotBlank()) {
                    MetadataRow(label = "Taken", value = item.dateTaken)
                }
                item.dimensionsLabel?.let { MetadataRow(label = "Dimensions", value = it) }
                item.sizeLabel?.let { MetadataRow(label = "Size", value = it) }
            }
        }
        if (item.tags.isNotEmpty()) {
            item {
                HorizontalDivider(modifier = Modifier.padding(horizontal = ContentPadding))
                Column(modifier = Modifier.padding(ContentPadding)) {
                    Text(
                        text = "Tags",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(TagRowSpacing),
                        verticalArrangement = Arrangement.spacedBy(TagRowSpacing),
                    ) {
                        item.tags.forEach { tag -> TagChip(tag = tag) }
                    }
                }
            }
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = ContentPadding))
            Column(modifier = Modifier.padding(ContentPadding)) {
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                )
                Spacer(modifier = Modifier.height(SectionSpacing))
            }
        }
    }
}

@Composable
private fun MetadataRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun TagChip(
    tag: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(TagCornerRadius),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier =
                Modifier.padding(
                    horizontal = TagHorizontalPadding,
                    vertical = TagVerticalPadding,
                ),
        )
    }
}

private fun previewItem() =
    DetailUiModel(
        id = "1",
        title = "Golden Gate at Sunset",
        imageUrl = "",
        description = "A breathtaking view of the Golden Gate Bridge during sunset.",
        authorName = "Jane Photographer",
        views = "12,345",
        dateUploaded = "Jan 15, 2024",
        dateTaken = "2024-01-14",
        photoPageUrl = "",
        dimensionsLabel = "1600 × 1067",
        sizeLabel = "Large 1600",
        imageAspectRatio = 1600f / 1067f,
        tags = listOf("landscape", "bridge", "sunset", "sanfrancisco"),
    )

@Preview(showBackground = true)
@Composable
private fun DetailContentPreview() {
    FlickerGalleryTheme {
        DetailContent(
            uiState = DetailUiState(item = previewItem()),
            onEvent = {},
            onNavigateBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailLoadingPreview() {
    FlickerGalleryTheme {
        DetailContent(uiState = DetailUiState(isLoading = true), onEvent = {}, onNavigateBack = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailErrorPreview() {
    FlickerGalleryTheme {
        DetailContent(
            uiState = DetailUiState(errorMessage = "Unable to load content. Please check your connection."),
            onEvent = {},
            onNavigateBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailEmptyPreview() {
    FlickerGalleryTheme {
        DetailContent(uiState = DetailUiState(isEmpty = true), onEvent = {}, onNavigateBack = {})
    }
}
