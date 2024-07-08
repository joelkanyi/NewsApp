package io.github.joelkanyi.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import kotlinx.serialization.Serializable

@Serializable
object SearchNews

@Composable
fun SearchNewsScreen(
    navController: NavController,
) {
    SearchNewsScreenContent(
        onClickBack = {
            navController.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNewsScreenContent(
    onClickBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        onClickBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(text = "Search News")
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Search News",
            )
        }
    }
}

@Preview
@Composable
fun SearchNewsScreenPreview() {
    NewsAppTheme {
        SearchNewsScreenContent(
            onClickBack = {}
        )
    }
}