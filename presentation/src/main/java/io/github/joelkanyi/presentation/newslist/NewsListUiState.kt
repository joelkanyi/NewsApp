package io.github.joelkanyi.presentation.newslist

import androidx.paging.PagingData
import io.github.joelkanyi.domain.model.News
import kotlinx.coroutines.flow.Flow

data class NewsListUiState(
    val news: Flow<PagingData<News>>? = null,
    val showNewsFilters: Boolean = false,
    val showCountryDialog: Boolean = false,
    val selectedCountry: String? = newsCountries.first(),
    val selectedCategory: String? = null,
    val initialized: Boolean = false,
)

val newsCategories =
    listOf(
        // "All News",
        "Business",
        "Entertainment",
        "General",
        "Health",
        "Science",
        "Sports",
        "Technology",
    )

val newsCountries =
    listOf(
        "United States",
        "United Kingdom",
        "Australia",
        "Canada",
        "Kenya",
        "India",
        "Germany",
        "France",
        "Italy",
        "Netherlands",
        "Norway",
        "Sweden",
        "China",
        "Japan",
        "South Korea",
        "Russia",
        "Brazil",
        "Argentina",
        "Mexico",
        "South Africa",
        "Nigeria",
        "Egypt",
        "Saudi Arabia",
        "United Arab Emirates",
        "Kuwait",
    )
