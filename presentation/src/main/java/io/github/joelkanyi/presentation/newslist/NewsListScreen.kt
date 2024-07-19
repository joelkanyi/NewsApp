package io.github.joelkanyi.presentation.newslist

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.joelkanyi.designsystem.components.BottomSheet
import io.github.joelkanyi.designsystem.components.EmptyStateComponent
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.presentation.R
import io.github.joelkanyi.presentation.components.NewsList
import io.github.joelkanyi.presentation.navigation.Destinations
import kotlinx.coroutines.launch

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
                    navController.navigate(Destinations.NewsDetails(action.news))
                }

                NewsListUiAction.NavigateToSearchNews -> {
                    navController.navigate(Destinations.SearchNews)
                }

                NewsListUiAction.ShowCountriesDialog -> {
                    viewModel.setCountriesDialogState(true)
                }

                NewsListUiAction.DismissCountriesDialog -> {
                    viewModel.setCountriesDialogState(false)
                }

                is NewsListUiAction.SelectCountry -> {
                    viewModel.setCountriesDialogState(false)
                    viewModel.selectCountry(action.country)
                }

                is NewsListUiAction.SelectCategory -> {
                }

                is NewsListUiAction.ApplyFilters -> {
                    action.category?.let { viewModel.selectCategory(it) }

                    viewModel.getNews(
                        country = action.country,
                        category = action.category
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreenContent(
    uiState: NewsListUiState,
    onAction: (NewsListUiAction) -> Unit
) {
    val newsPaging = uiState.news?.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

    Scaffold(
        modifier = Modifier
            .testTag(stringResource(R.string.news_list_screen))
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.selectedCategory ?: stringResource(R.string.all_news),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.testTag(stringResource(R.string.search_icon)),
                        onClick = {
                            onAction(NewsListUiAction.NavigateToSearchNews)
                        }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }

                    IconButton(
                        modifier = Modifier.testTag(stringResource(R.string.filters_icon)),
                        onClick = {
                            scope.launch {
                                bottomSheetState.show()
                            }
                        }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Sort,
                            contentDescription = stringResource(R.string.filters)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (newsPaging != null) {
                NewsList(
                    newsPaging,
                    onClickNews = {
                        onAction(NewsListUiAction.NavigateToNewsDetails(it))
                    }
                )
            } else {
                EmptyStateComponent(
                    modifier = Modifier.align(Alignment.Center),
                    message = stringResource(R.string.no_news_available)
                )
            }
        }
    }

    if (bottomSheetState.isVisible) {
        BottomSheet(
            modifier =
            Modifier
                .testTag(stringResource(R.string.news_filters)),
            bottomSheetState = bottomSheetState,
            shape = RoundedCornerShape(0),
            onDismissRequest = {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        ) {
            NewsFiltersContent(
                selectedCountry = uiState.selectedCountry,
                selectedCategory = uiState.selectedCategory,
                categories = uiState.newsCategories,
                onClickSelectCountry = {
                    onAction(NewsListUiAction.ShowCountriesDialog)
                },
                onApplyFilter = { temporaryCategory ->
                    scope.launch {
                        bottomSheetState.hide()
                    }
                    onAction(
                        NewsListUiAction.ApplyFilters(
                            country = uiState.selectedCountry,
                            category = temporaryCategory
                        )
                    )
                }
            )
        }
    }

    if (uiState.showCountryDialog) {
        CountriesDialog(
            countries = uiState.newsCountries,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsFiltersContent(
    modifier: Modifier = Modifier,
    selectedCountry: String?,
    selectedCategory: String?,
    categories: List<String>,
    onClickSelectCountry: () -> Unit,
    onApplyFilter: (selectedCategory: String?) -> Unit
) {
    var temporaryCategory by rememberSaveable {
        mutableStateOf(selectedCategory)
    }

    LazyColumn(
        modifier =
        modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_country),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Row(
                    modifier =
                    modifier
                        .fillMaxWidth()
                        .clickable { onClickSelectCountry() },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCountry ?: stringResource(R.string.select_country),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_category),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    categories.forEach { category ->
                        CategoryItem(
                            isSelected = category == temporaryCategory,
                            name = category,
                            onClick = {
                                temporaryCategory = category
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
                onClick = {
                    onApplyFilter(
                        temporaryCategory
                    )
                }
            ) {
                Text(
                    text = stringResource(R.string.okay),
                    style = MaterialTheme.typography.labelMedium
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
        modifier
            .border(
                BorderStroke(
                    width = .5.dp,
                    color =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            )
            .background(
                color =
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier =
            Modifier.padding(
                vertical = 6.dp,
                horizontal = 16.dp
            ),
            text = name,
            style =
            MaterialTheme.typography.bodyMedium.copy(
                color =
                if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
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
        modifier =
        Modifier
            .testTag(stringResource(R.string.countries_dialog))
            .fillMaxHeight(.5f),
        onDismissRequest = onDismiss,
        tonalElevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        title = {
            Text(
                text = stringResource(R.string.select_country),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            LazyColumn(
                modifier =
                modifier
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
        modifier =
        modifier
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
                tint = MaterialTheme.colorScheme.primary
            )
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
