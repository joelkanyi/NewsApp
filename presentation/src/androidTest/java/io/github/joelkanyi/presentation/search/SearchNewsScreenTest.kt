package io.github.joelkanyi.presentation.search

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.presentation.model.NewsUiModel
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchNewsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun searchStringIsEntered_WhenUserTypesInSearchBar() {
        val searchValue = "Search News Here"

        composeTestRule.setContent {
            NewsAppTheme {
                SearchNewsScreenContent(
                    uiState = SearchNewsUiState(
                        searchValue = searchValue
                    ),
                    onClickBack = {},
                    onClickNews = {},
                    onSearchValueChange = {
                    }
                )
            }
        }

        composeTestRule.onNodeWithText(searchValue)
            .assertIsDisplayed()
    }

    @Test
    fun searchStringIsCleared_WhenUserClicksOnClearButton() {
        var searchValue = "Search News Here"

        composeTestRule.setContent {
            NewsAppTheme {
                SearchNewsScreenContent(
                    uiState = SearchNewsUiState(
                        searchValue = searchValue
                    ),
                    onClickBack = {},
                    onClickNews = {},
                    onSearchValueChange = {
                        searchValue = ""
                    }
                )
            }
        }

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.clear_search))
            .performClick()

        composeTestRule.onNodeWithText(searchValue)
            .assertDoesNotExist()
    }

    @Test
    fun newsListIsDisplayed_WhenUserSearchesForNews() {
        val searchValue = "Search News Here"

        composeTestRule.setContent {
            NewsAppTheme {
                SearchNewsScreenContent(
                    uiState = SearchNewsUiState(
                        searchValue = searchValue,
                        news = flowOf(PagingData.from(
                            listOf(
                                NewsUiModel(
                                    title = "Title",
                                    description = "Description",
                                    url = "https://www.example.com",
                                    imageUrl = "https://www.example.com/image.jpg",
                                    source = "Source",
                                    publishedAt = "2021-09-01T00:00:00Z",
                                    content = "Content",
                                    author = "Author"
                                )
                            )
                        ))
                    ),
                    onClickBack = {},
                    onClickNews = {},
                    onSearchValueChange = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Title")
            .assertIsDisplayed()
    }

    @Test
    fun emptyStateIsDisplayed_WhenNewsListIsEmpty() {
        composeTestRule.setContent {
            NewsAppTheme {
                SearchNewsScreenContent(
                    uiState = SearchNewsUiState(),
                    onClickBack = {},
                    onClickNews = {},
                    onSearchValueChange = {}
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.no_news_available))
            .assertIsDisplayed()
    }

    @Test
    fun loadingStateIsDisplayed_WhenNewsListIsLoading() {
        composeTestRule.setContent {
            NewsAppTheme {
                SearchNewsScreenContent(
                    uiState = SearchNewsUiState(news = flowOf(PagingData.empty())),
                    onClickBack = {},
                    onClickNews = {},
                    onSearchValueChange = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(io.github.joelkanyi.designsystem.R.string.loading_state_component))
            .assertIsDisplayed()
    }
}
