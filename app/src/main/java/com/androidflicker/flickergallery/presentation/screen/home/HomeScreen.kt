package com.androidflicker.flickergallery.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androidflicker.flickergallery.domain.model.Photo
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.error != null ->
                ErrorState(
                    message = uiState.error,
                    onRetry = { onEvent(HomeEvent.RetryLoad) },
                )
            else -> PhotoList(photos = uiState.photos)
        }
    }
}

@Composable
private fun PhotoList(photos: List<Photo>) {
    if (photos.isEmpty()) {
        Text(
            text = "No photos found",
            style = MaterialTheme.typography.bodyLarge,
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(photos, key = { it.id }) { photo ->
                PhotoItem(photo = photo)
            }
        }
    }
}

@Composable
private fun PhotoItem(photo: Photo) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = photo.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "by ${photo.owner}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    FlickerGalleryTheme {
        HomeContent(
            uiState =
                HomeUiState(
                    photos =
                        listOf(
                            Photo(id = "1", title = "Golden Gate Bridge", imageUrl = "", owner = "user123"),
                            Photo(id = "2", title = "Sunset Over Tokyo", imageUrl = "", owner = "user456"),
                        ),
                ),
            onEvent = {},
        )
    }
}
