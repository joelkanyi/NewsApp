package io.github.joelkanyi.presentation.newslist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.presentation.R
import io.github.joelkanyi.presentation.model.NewsUiModel
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class NewsListScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showLoadingState_whenNewsListIsLoading() {
        /**
         * Paging library needs to determine the actual state of the data. When you
         * create an empty PagingData, it doesn't trigger a fetch, hence it remains
         * in a loading state.
         */
        val loadingNews = PagingData.empty<NewsUiModel>()

        composeTestRule.setContent {
            NewsAppTheme {
                NewsListScreenContent(
                    uiState = NewsListUiState(news = flowOf(loadingNews)),
                    onAction = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(io.github.joelkanyi.designsystem.R.string.loading_state_component))
            .assertIsDisplayed()
    }

    @Test
    fun showEmptyState_whenNewsListIsEmpty() {
        composeTestRule.setContent {
            NewsAppTheme {
                NewsListScreenContent(
                    uiState = NewsListUiState(),
                    onAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.no_news_available))
            .assertIsDisplayed()
    }

    @Test
    fun showNewsList_whenNewsListIsNotEmpty() {
        val newsList = listOf(
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

        val news = PagingData.from(newsList)

        composeTestRule.setContent {
            NewsAppTheme {
                NewsListScreenContent(
                    uiState = NewsListUiState(news = flowOf(news)),
                    onAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Title 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title 2").assertIsDisplayed()
    }

    @Test
    fun showNewsFilters_whenFiltersIconIsClicked() {
        composeTestRule.setContent {
            NewsAppTheme {
                NewsListScreenContent(
                    uiState = NewsListUiState(),
                    onAction = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.filters_icon))
            .performClick()

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.news_filters))
            .assertIsDisplayed()
    }

    @Test
    fun showCountriesDialog_whenShowCountriesDialogIsTrue() {
        composeTestRule.setContent {
            NewsAppTheme {
                NewsListScreenContent(
                    uiState = NewsListUiState(showCountryDialog = true),
                    onAction = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.countries_dialog))
            .assertIsDisplayed()
    }

    @Test
    fun topAppBar_ShowsCorrectTitle() {
        val title = "Technology News"

        composeTestRule.setContent {
            NewsAppTheme {
                NewsListScreenContent(
                    uiState = NewsListUiState(
                        selectedCategory = title,
                    ),
                    onAction = {}
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }
}
