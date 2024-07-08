package io.github.joelkanyi.presentation.newslist

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import io.github.joelkanyi.designsystem.components.BottomSheet
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.presentation.newsdetails.NewsDetails
import io.github.joelkanyi.presentation.search.SearchNews
import io.github.joelkanyi.presentation.utils.ErrorResponse
import kotlinx.serialization.Serializable
import retrofit2.HttpException
import java.io.IOException
import java.io.Reader

@Serializable
object NewsList

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NewsListScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is NewsListUiAction.NavigateToNewsDetails -> {
                    navController.navigate(NewsDetails(action.news))
                }

                NewsListUiAction.DismissFilters -> {
                    viewModel.toggleFilters(false)
                }

                NewsListUiAction.ShowFilters -> {
                    viewModel.toggleFilters(true)
                }

                NewsListUiAction.NavigateToSearchNews -> {
                    navController.navigate(SearchNews)
                }

                NewsListUiAction.ShowCountriesDialog -> {
                    viewModel.toggleCountriesDialog(true)
                }

                NewsListUiAction.DismissCountriesDialog -> {
                    viewModel.toggleCountriesDialog(false)
                }

                is NewsListUiAction.SelectCountry -> {
                    viewModel.selectCountry(action.country)
                }

                is NewsListUiAction.SelectCategory -> {
                    viewModel.selectCategory(action.category)
                }

                is NewsListUiAction.ApplyFilters -> {
                    viewModel.toggleFilters(false)
                    viewModel.getNews(
                        country = action.country,
                        category = action.category
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreenContent(
    uiState: NewsListUiState,
    onAction: (NewsListUiAction) -> Unit,
) {
    val newsPaging = uiState.news.collectAsLazyPagingItems()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "News App",
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    IconButton(onClick = {
                        onAction(NewsListUiAction.NavigateToSearchNews)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    }

                    IconButton(onClick = {
                        onAction(NewsListUiAction.ShowFilters)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Sort,
                            contentDescription = "More options",
                        )
                    }
                }
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
                        NewsItem(
                            news = news,
                            onClick = {
                                onAction(NewsListUiAction.NavigateToNewsDetails(news))
                            },
                        )
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

    if (uiState.showNewsFilters) {
        BottomSheet(
            modifier = Modifier.fillMaxHeight(.5f),
            bottomSheetState = bottomSheetState,
            shape = RoundedCornerShape(0),
            onDismissRequest = {
                onAction(NewsListUiAction.DismissFilters)
            },
        ) {
            NewsFiltersContent(
                selectedCountry = uiState.selectedCountry,
                selectedCategory = uiState.selectedCategory,
                onClickSelectCountry = {
                    onAction(NewsListUiAction.ShowCountriesDialog)
                },
                onSelectCategory = {
                    onAction(NewsListUiAction.SelectCategory(it))
                },
                onApplyFilter = {
                    onAction(
                        NewsListUiAction.ApplyFilters(
                            country = uiState.selectedCountry,
                            category = uiState.selectedCategory
                        )
                    )
                }
            )
        }
    }

    if (uiState.showCountryDialog) {
        CountriesDialog(
            countries = newsCountries,
            selectedCountry = uiState.selectedCountry,
            onDismiss = {
                onAction(NewsListUiAction.DismissCountriesDialog)
            },
            onSelectCountry = {
                onAction(NewsListUiAction.SelectCountry(it))
            }
        )
    }
}

@Composable
fun NewsItem(
    news: News,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick,
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsFiltersContent(
    modifier: Modifier = Modifier,
    selectedCountry: String?,
    selectedCategory: String?,
    onClickSelectCountry: () -> Unit,
    onSelectCategory: (String) -> Unit,
    onApplyFilter: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Select Country",
                    style = MaterialTheme.typography.titleMedium,
                )

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { onClickSelectCountry() },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCountry ?: "Select country",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Select Category",
                    style = MaterialTheme.typography.titleMedium,
                )

                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    newsCategories.forEach { category ->
                        CategoryItem(
                            isSelected = category == selectedCategory,
                            name = category,
                            onClick = {
                                onSelectCategory(category)
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onApplyFilter
            ) {
                Text(
                    text = "Okay",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}


@Composable
fun CategoryItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(
                BorderStroke(
                    width = .5.dp,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            )
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary.copy(.1f)
                } else {
                    Color.Transparent
                },
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = 6.dp,
                horizontal = 16.dp,
            ),
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            ),
        )
    }
}

@Composable
fun CountriesDialog(
    countries: List<String>,
    selectedCountry: String?,
    onDismiss: () -> Unit,
    onSelectCountry: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = Modifier.fillMaxHeight(.5f),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Country",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                countries.forEach { country ->
                    item {
                        CountryItem(
                            country = country,
                            onClick = {
                                onSelectCountry(country)
                            },
                            isSelected = country == selectedCountry
                        )
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun CountryItem(
    onClick: () -> Unit,
    isSelected: Boolean,
    country: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = country,
            style = MaterialTheme.typography.bodyMedium
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.Green,
            )
        }
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

@Preview
@Composable
fun NewsListScreenPreview() {
    NewsAppTheme {
        NewsListScreenContent(
            uiState = NewsListUiState(),
            onAction = {}
        )
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