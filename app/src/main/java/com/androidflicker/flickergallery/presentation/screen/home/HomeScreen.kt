package com.androidflicker.flickergallery.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeContent()
}

@Composable
private fun HomeContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Android Flicker App",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    FlickerGalleryTheme {
        HomeContent()
    }
}
