package io.github.joelkanyi.presentation.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import io.github.joelkanyi.designsystem.theme.NewsAppTheme

@Composable
fun FavoritesScreen(
    navController: NavController
) {
    FavoritesScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenContent() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorites",
                        style = MaterialTheme.typography.titleMedium
                    )
                })
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Favorites",
            )
        }
    }
}

@Preview
@Composable
private fun FavoritesScreenPreview() {
    NewsAppTheme {
        FavoritesScreenContent()
    }
}
