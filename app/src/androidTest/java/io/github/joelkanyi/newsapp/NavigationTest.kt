package io.github.joelkanyi.newsapp

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.joelkanyi.presentation.navigation.AppNavHost
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AppNavHost(navController = navController)
        }
    }

    @Test
    fun appNavHost_verifyStartDestination() {
        val newsListScreenTag = composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.news_list_screen)

        composeTestRule.onNodeWithTag(newsListScreenTag)
            .assertExists()
    }

    @Test
    fun navigateToSearchScreen_AfterClickingSearchIcon() {
        val searchScreenTag = composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.search_news_screen)
        val searchIconTag = composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.search_icon)

        composeTestRule.onNodeWithTag(searchIconTag)
            .performClick()

        composeTestRule.onNodeWithTag(searchScreenTag)
            .assertExists()
    }

    @Test
    fun navigateBackToNewsListScreen_AfterClickingBackIconInSearchScreen() {
        val searchScreenTag = composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.search_news_screen)
        val backIconTag = composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.back_icon)
        val newsListScreenTag = composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.news_list_screen)
        val searchIconTag = composeTestRule.activity.getString(io.github.joelkanyi.presentation.R.string.search_icon)

        composeTestRule.onNodeWithTag(searchIconTag)
            .performClick()
        composeTestRule.onNodeWithTag(searchScreenTag)
            .assertExists()
        composeTestRule.onNodeWithTag(backIconTag)
            .performClick()
        composeTestRule.onNodeWithTag(newsListScreenTag)
            .assertExists()
    }
}
