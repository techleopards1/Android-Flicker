package com.androidflicker.flickergallery.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androidflicker.flickergallery.presentation.component.SwimlaneSection
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

private val HomeTopPadding = 16.dp
private val HomeBottomPadding = 32.dp
private val SectionSpacing = 28.dp
private val HeaderHorizontalPadding = 16.dp
private val HeaderVerticalPadding = 8.dp
private val ErrorRetrySpacing = 16.dp

private const val PREVIEW_ITEM_COUNT = 6
private const val PREVIEW_CATEGORY_COUNT = 10

@Composable
fun HomeScreen(
    onNavigateToDetails: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToDetails = onNavigateToDetails,
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToDetails: (String) -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> HomeLoadingState()
            uiState.errorMessage != null ->
                HomeErrorState(
                    message = uiState.errorMessage,
                    onRetry = { onEvent(HomeEvent.RetryLoad) },
                )
            else ->
                HomeSections(
                    uiState = uiState,
                    onNavigateToDetails = onNavigateToDetails,
                )
        }
    }
}

@Composable
private fun HomeSections(
    uiState: HomeUiState,
    onNavigateToDetails: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = HomeTopPadding, bottom = HomeBottomPadding),
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        item { HomeHeader() }

        items(
            items = uiState.categories,
            key = { it.id },
        ) { category ->
            SwimlaneSection(
                title = category.title,
                items = category.items,
                onItemClick = { onNavigateToDetails(it.id) },
                errorMessage = category.errorMessage,
            )
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = HeaderHorizontalPadding, vertical = HeaderVerticalPadding),
    ) {
        Text(
            text = "Flicker Gallery",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Discover stunning photography",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
    }
}

@Composable
private fun HomeLoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HomeErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(ErrorRetrySpacing))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

private fun previewCategories(count: Int = PREVIEW_CATEGORY_COUNT): List<HomeCategoryUiModel> {
    val names = listOf("Nature", "Animals", "Architecture", "Historical", "Popular", "Trending", "Technology", "Space", "Travel", "Recent")
    return names.take(count).mapIndexed { index, name ->
        HomeCategoryUiModel(
            id = name.lowercase(),
            title = name,
            items = List(PREVIEW_ITEM_COUNT) { i -> HomeItemUiModel("${name}_$i", "Photo $i", "") },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    FlickerGalleryTheme {
        HomeContent(
            uiState = HomeUiState(categories = previewCategories()),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeLoadingPreview() {
    FlickerGalleryTheme {
        HomeContent(uiState = HomeUiState(isLoading = true), onEvent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeErrorPreview() {
    FlickerGalleryTheme {
        HomeContent(uiState = HomeUiState(errorMessage = "No internet connection"), onEvent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeEmptyCategoryPreview() {
    FlickerGalleryTheme {
        HomeContent(
            uiState =
                HomeUiState(
                    categories =
                        listOf(
                            HomeCategoryUiModel(id = "nature", title = "Nature", items = emptyList()),
                            HomeCategoryUiModel(
                                id = "space",
                                title = "Space",
                                items = emptyList(),
                                errorMessage = "No internet connection",
                            ),
                        ),
                ),
            onEvent = {},
        )
    }
}
