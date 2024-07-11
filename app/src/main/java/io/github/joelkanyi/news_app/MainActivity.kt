package io.github.joelkanyi.news_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.presentation.newsdetails.NewsDetails
import io.github.joelkanyi.presentation.newsdetails.NewsDetailsScreen
import io.github.joelkanyi.presentation.newslist.NewsList
import io.github.joelkanyi.presentation.newslist.NewsListScreen
import io.github.joelkanyi.presentation.search.SearchNews
import io.github.joelkanyi.presentation.search.SearchNewsScreen
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = NewsList) {
                    val NewsParameterType = object : NavType<News>(
                        isNullableAllowed = false
                    ) {
                        override fun put(bundle: Bundle, key: String, value: News) {
                            bundle.putString(key, Gson().toJson(value))
                        }

                        override fun get(bundle: Bundle, key: String): News {
                            return Gson().fromJson(bundle.getString(key), News::class.java)
                        }

                        override fun parseValue(value: String): News {
                            return Gson().fromJson(value, News::class.java)
                        }

                        // Only required when using Navigation 2.4.0-alpha07 and lower
                        override val name = "News"
                    }

                    composable<NewsList> {
                        NewsListScreen(
                            navController = navController
                        )
                    }

                    composable<NewsDetails>(
                        typeMap = mapOf(typeOf<News>() to NewsParameterType)
                    ) { backStackEntry ->
                        val news = backStackEntry.toRoute<News>()
                        NewsDetailsScreen(
                            navController = navController,
                            news = news
                        )
                    }

                    composable<SearchNews> {
                        SearchNewsScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}