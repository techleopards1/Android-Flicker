package com.androidflicker.flickergallery.presentation.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

private val LoadingTopPadding = 16.dp
private val LoadingBottomPadding = 32.dp
private val LoadingSectionSpacing = 28.dp
private const val SHIMMER_ROW_COUNT = 5

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    val shimmerBrush = rememberShimmerBrush()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = LoadingTopPadding, bottom = LoadingBottomPadding),
        verticalArrangement = Arrangement.spacedBy(LoadingSectionSpacing),
        userScrollEnabled = false,
    ) {
        items(SHIMMER_ROW_COUNT) {
            ShimmerSwimlaneRow(shimmerBrush = shimmerBrush)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingStatePreview() {
    FlickerGalleryTheme {
        LoadingState()
    }
}
