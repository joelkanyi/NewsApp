package io.github.joelkanyi.presentation.navigation

import io.github.joelkanyi.presentation.model.NewsUiModel
import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    object NewsList

    @Serializable
    object SearchNews

    @Serializable
    data class NewsDetails(val news: NewsUiModel)

    @Serializable
    data object Favorites

    @Serializable
    object Settings

    @Serializable
    object Language
}
