package io.github.joelkanyi.presentation.newsdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.presentation.components.NewsImage
import io.github.joelkanyi.presentation.model.NewsUiModel
import io.github.joelkanyi.presentation.utils.toHumanReadableDateTIme
import timber.log.Timber

@Composable
fun NewsDetailsScreen(
    news: NewsUiModel,
    navController: NavController,
    viewModel: NewsDetailsViewModel = hiltViewModel()
) {
    val addedToFavorites by viewModel.isFavorite(news).collectAsState(false)
    Timber.e("addedToFavorites: $addedToFavorites")

    NewsDetailsScreenContent(
        news = news,
        addedToFavorites = addedToFavorites,
        onClickBack = {
            navController.navigateUp()
        },
        onClickShare = { },
        onClickFavorite = {
            if (addedToFavorites) {
                viewModel.removeFavorite(news)
            } else {
                viewModel.addFavorite(news)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailsScreenContent(
    news: NewsUiModel,
    addedToFavorites: Boolean,
    onClickBack: () -> Unit,
    onClickShare: () -> Unit,
    onClickFavorite: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = {
                        onClickBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {},
                actions = {
                    IconButton(onClick = {
                        onClickShare()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share"
                        )
                    }

                    IconButton(onClick = {
                        onClickFavorite()
                    }) {
                        Icon(
                            imageVector =
                            if (addedToFavorites) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier =
            Modifier
                .padding(it)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                Text(
                    text =
                    if (news.author.isEmpty()) {
                        news.source
                    } else {
                        "By ${news.author}, ${news.source}"
                    },
                    style =
                    MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                Text(
                    text = news.publishedAt.toHumanReadableDateTIme(),
                    style =
                    MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
            }

            item {
                NewsImage(
                    imageUrl = news.imageUrl,
                    contentDescription = news.title,
                    modifier =
                    Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                )
            }

            item {
                Text(
                    text = news.description,
                    style =
                    MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
            }

            item {
                Text(
                    text = news.content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun NewsDetailsScreenPreview() {
    NewsAppTheme {
        NewsDetailsScreenContent(
            addedToFavorites = true,
            news =
            NewsUiModel(
                title = "Title",
                description = "Description",
                content = "Content",
                imageUrl = "https://example.com/image.jpg",
                source = "Source",
                publishedAt = "2021-09-01T12:00:00Z",
                author = "Author",
                url = "https://example.com"
            ),
            onClickBack = {},
            onClickShare = {},
            onClickFavorite = {}
        )
    }
}
