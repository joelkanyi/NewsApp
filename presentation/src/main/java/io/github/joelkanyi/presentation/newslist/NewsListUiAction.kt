/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.presentation.newslist

import io.github.joelkanyi.presentation.model.NewsUiModel

sealed interface NewsListUiAction {

    data class NavigateToNewsDetails(val news: NewsUiModel) : NewsListUiAction

    data object NavigateToSearchNews : NewsListUiAction

    data object ShowCountriesDialog : NewsListUiAction

    data object DismissCountriesDialog : NewsListUiAction

    data class SelectCountry(val country: String) : NewsListUiAction

    data class SelectCategory(val category: String) : NewsListUiAction

    data class ApplyFilters(val country: String?, val category: String?) : NewsListUiAction
}
