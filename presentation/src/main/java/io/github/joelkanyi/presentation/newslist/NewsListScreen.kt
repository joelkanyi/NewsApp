package io.github.joelkanyi.presentation.newslist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import io.github.joelkanyi.presentation.utils.ErrorResponse
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.domain.model.News
import retrofit2.HttpException
import java.io.IOException
import java.io.Reader

@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NewsListScreenContent(
        uiState = uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreenContent(
    uiState: NewsListUiState,
) {
    val newsPaging = uiState.news.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "News App",
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(newsPaging.itemCount) {
                    val news = newsPaging[it]
                    if (news != null) {
                        NewsItem(news = news)
                    }
                }
                newsPaging.loadState.let { loadState ->
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.refresh is LoadState.NotLoading && newsPaging.itemCount < 1 -> {
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No news available",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = loadState.refresh as LoadState.Error
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = error.getPagingError(),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .align(Alignment.Center),
                                        strokeWidth = 2.dp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    news: News,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(.32f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(news.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = news.title,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = io.github.joelkanyi.designsystem.R.drawable.image_placeholder),
                            contentDescription = "Error loading image",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            )

            Column(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = news.publishedAt,
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Text(
                        text = news.source,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NewsCardPreview() {
    NewsAppTheme {
        NewsItem(
            news = News(
                title = "Title of the news item goes here and it can be long and wrap to the next line max 2 lines, the rest will be truncated. and we use ellipsis to indicate that it is truncated.",
                imageUrl = "https://example.com/image.jpg",
                publishedAt = "2022-01-01",
                source = "Source",
                author = "Author",
                description = "Description",
                url = "https://example.com",
                content = "Content"
            )
        )
    }
}

@Preview
@Composable
fun NewsListScreenPreview() {
    NewsAppTheme {
        NewsListScreenContent(
            uiState = NewsListUiState(),
        )
    }
}

fun LoadState.Error.getPagingError(): String {
    return when (val err = this.error) {
        is HttpException -> {
            errorMessage(err)
        }

        is IOException -> {
            "Network error"
        }

        else -> {
            "Unknown error"
        }
    }
}


fun errorMessage(httpException: HttpException): String {
    val errorResponse = convertErrorBody<ErrorResponse>(httpException)
    Log.e("errorMessage", "Error Response: $errorResponse")
    return errorResponse?.message ?: "Unknown error"
}

inline fun <reified T> convertErrorBody(throwable: HttpException): T? {
    return try {
        throwable.response()?.errorBody()?.charStream()?.use { it.readerToObject() }
    } catch (e: JsonParseException) {
        null
    }
}

inline fun <reified T> Reader.readerToObject(): T {
    val gson = GsonBuilder()
        .create()
    return gson.fromJson(this, T::class.java)
}

