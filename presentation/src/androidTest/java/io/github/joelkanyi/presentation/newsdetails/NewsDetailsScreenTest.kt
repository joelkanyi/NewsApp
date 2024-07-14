package io.github.joelkanyi.presentation.newsdetails

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.joelkanyi.presentation.R
import io.github.joelkanyi.presentation.model.NewsUiModel
import io.github.joelkanyi.presentation.utils.toHumanReadableDateTIme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDetailsScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showNewsDetailsScreen_whenNewsIsProvided() {
        val publishedAt = "2022-01-01T00:00:00Z"
        val author = "Author"
        val source = "Source"
        val title = "Title"
        val description = "Description"
        val imageUrl = "https://www.example.com/image.jpg"
        val content = "Content"

        composeTestRule.setContent {
            NewsDetailsScreenContent(
                news = NewsUiModel(
                    title = title,
                    description = description,
                    url = "https://www.example.com",
                    imageUrl = imageUrl,
                    publishedAt = publishedAt,
                    source = source,
                    author = author,
                    content = content
                ),
                addedToFavorites = false,
                onClickBack = { },
                onClickFavorite = { },
                onClickShare = { }
            )
        }

        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.by, author, source)).assertExists()
        composeTestRule.onNodeWithText(publishedAt.toHumanReadableDateTIme()).assertExists()
        composeTestRule.onNodeWithText(description).assertExists()
        composeTestRule.onNodeWithText(content).assertExists()
    }

    @Test
    fun favoriteIconIsFilled_WhenNewsIsFavorite() {
        val publishedAt = "2022-01-01T00:00:00Z"
        val author = "Author"
        val source = "Source"
        val title = "Title"
        val description = "Description"
        val imageUrl = "https://www.example.com/image.jpg"
        val content = "Content"

        composeTestRule.setContent {
            NewsDetailsScreenContent(
                news = NewsUiModel(
                    title = title,
                    description = description,
                    url = "https://www.example.com",
                    imageUrl = imageUrl,
                    publishedAt = publishedAt,
                    source = source,
                    author = author,
                    content = content
                ),
                addedToFavorites = true,
                onClickBack = { },
                onClickFavorite = { },
                onClickShare = { }
            )
        }

        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.added_to_favorite_icon)).assertExists()
    }
}
