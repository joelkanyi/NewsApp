package io.github.joelkanyi.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.joelkanyi.designsystem.components.EmptyStateComponent
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.presentation.components.NewsList
import kotlinx.serialization.Serializable

@Serializable
object SearchNews

@Composable
fun SearchNewsScreen(
    navController: NavController,
    viewModel: SearchNewsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchNewsScreenContent(
        uiState = uiState,
        onClickNews = {
        },
        onClickBack = {
            navController.navigateUp()
        },
        onSearchValueChange = { searchString ->
            viewModel.updateSearchValue(searchString)
            if (searchString.trim().isNotEmpty()) {
                viewModel.getNews(searchString)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNewsScreenContent(
    uiState: SearchNewsUiState,
    onClickBack: () -> Unit,
    onClickNews: (News) -> Unit,
    onSearchValueChange: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            TopAppBar(
                title = {
                    TextField(
                        modifier = Modifier.focusRequester(focusRequester),
                        value = uiState.searchValue,
                        onValueChange = onSearchValueChange,
                        placeholder = {
                            Text(
                                text = "Search...",
                                style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                                    ),
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors =
                            TextFieldDefaults.colors(
                                disabledContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onClickBack()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSearchValueChange("")
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->

        val newsPaging = uiState.news?.collectAsLazyPagingItems()

        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            if (newsPaging != null) {
                NewsList(
                    newsPaging,
                    onClickNews = {
                        onClickNews(it)
                    },
                )
            } else {
                EmptyStateComponent(
                    modifier = Modifier.align(Alignment.Center),
                    message = "No news available",
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchNewsScreenPreview() {
    NewsAppTheme {
        SearchNewsScreenContent(
            uiState = SearchNewsUiState(),
            onClickBack = {},
            onClickNews = {},
            onSearchValueChange = {},
        )
    }
}
