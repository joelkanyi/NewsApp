/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.joelkanyi.presentation.favorites.FavoritesScreen
import io.github.joelkanyi.presentation.language.LanguageScreen
import io.github.joelkanyi.presentation.model.NewsUiModel
import io.github.joelkanyi.presentation.newsdetails.NewsDetailsScreen
import io.github.joelkanyi.presentation.newslist.NewsListScreen
import io.github.joelkanyi.presentation.search.SearchNewsScreen
import io.github.joelkanyi.presentation.settings.SettingsScreen
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destinations.NewsList
    ) {
        composable<Destinations.NewsList> {
            NewsListScreen(
                navController = navController
            )
        }

        composable<Destinations.NewsDetails>(
            typeMap = mapOf(typeOf<NewsUiModel>() to NewsUiModelParameterType)
        ) { backStackEntry ->
            val news = backStackEntry.toRoute<Destinations.NewsDetails>().news
            NewsDetailsScreen(
                news = news,
                navController = navController
            )
        }

        composable<Destinations.SearchNews> {
            SearchNewsScreen(
                navController = navController
            )
        }

        composable<Destinations.Favorites> {
            FavoritesScreen(
                navController = navController
            )
        }

        composable<Destinations.Settings> {
            SettingsScreen(
                navController = navController
            )
        }

        composable<Destinations.Language> {
            LanguageScreen(
                navController = navController
            )
        }
    }
}
