package io.github.joelkanyi.presentation.newslist

import io.github.joelkanyi.domain.model.News

sealed interface NewsListUiAction {
    data object ShowFilters : NewsListUiAction
    data object DismissFilters : NewsListUiAction
    data class NavigateToNewsDetails(val news: News) : NewsListUiAction
    data object NavigateToSearchNews : NewsListUiAction
    data object ShowCountriesDialog : NewsListUiAction
    data object DismissCountriesDialog : NewsListUiAction
    data class SelectCountry(val country: String) : NewsListUiAction
    data class SelectCategory(val category: String) : NewsListUiAction
    data class ApplyFilters(val country: String?, val category: String?) : NewsListUiAction
}
