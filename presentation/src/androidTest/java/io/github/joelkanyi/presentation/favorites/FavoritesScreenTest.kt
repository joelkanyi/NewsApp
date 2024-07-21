/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.presentation.favorites

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.presentation.model.NewsUiModel
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showEmptyState_whenFavoritesIsEmpty() {
        composeTestRule.setContent {
            NewsAppTheme {
                FavoritesScreenContent(
                    favorites = emptyList(),
                    onNewsClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("No favorites yet").assertIsDisplayed()
    }

    @Test
    fun showFavorites_whenFavoritesIsNotEmpty() {
        val favorites = listOf(
            NewsUiModel(
                title = "Title 1",
                description = "Description 1",
                imageUrl = "https://example.com/image1.jpg",
                url = "https://example.com/news1",
                source = "Source 1",
                publishedAt = "2021-09-01T00:00:00Z",
                content = "Content 1",
                author = "Author 1"
            ),
            NewsUiModel(
                title = "Title 2",
                description = "Description 2",
                imageUrl = "https://example.com/image2.jpg",
                url = "https://example.com/news2",
                source = "Source 2",
                publishedAt = "2021-09-02T00:00:00Z",
                content = "Content 2",
                author = "Author 2"
            )
        )

        composeTestRule.setContent {
            NewsAppTheme {
                FavoritesScreenContent(
                    favorites = favorites,
                    onNewsClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Title 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title 2").assertIsDisplayed()
    }
}
