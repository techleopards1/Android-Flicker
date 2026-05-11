package com.androidflicker.flickergallery.presentation.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androidflicker.flickergallery.presentation.theme.FlickerGalleryTheme

private val ErrorMessageRetrySpacing = 16.dp
private val ErrorHorizontalPadding = 32.dp

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = ErrorHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(ErrorMessageRetrySpacing))
        Button(
            onClick = onRetry,
            modifier =
                Modifier.semantics {
                    contentDescription = "Retry loading content"
                },
        ) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    FlickerGalleryTheme {
        ErrorState(
            message = "Unable to load content. Please check your connection.",
            onRetry = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
