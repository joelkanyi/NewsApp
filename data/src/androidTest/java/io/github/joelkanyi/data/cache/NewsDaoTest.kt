/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.data.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class NewsDaoTest {
    private lateinit var newsDao: NewsDao
    private lateinit var db: NewsDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, NewsDatabase::class.java).build()
        newsDao = db.newsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun newsDao_insertNews() = runTest {
        // Given
        val expectedResult = NewsEntity(
            title = "title",
            description = "description",
            content = "url",
            imageUrl = "urlToImage",
            source = "content",
            publishedAt = "publishedAt",
            author = "source",
            url = "url"
        )

        // When
        newsDao.insert(expectedResult)

        // Then
        newsDao.getAll().test {
            val news = awaitItem().first()
            assertThat(expectedResult).isEqualTo(news)
        }
    }

    @Test
    fun newsDao_deleteNews() = runTest {
        // Given
        val expectedResult = NewsEntity(
            title = "title",
            description = "description",
            content = "url",
            imageUrl = "urlToImage",
            source = "content",
            publishedAt = "publishedAt",
            author = "source",
            url = "url"
        )

        // When
        newsDao.insert(expectedResult)
        newsDao.delete(expectedResult)

        // Then
        newsDao.getAll().test {
            val news = awaitItem()
            assertThat(news).isEmpty()
        }
    }

    @Test
    fun newsDao_isFavorite() = runTest {
        // Given
        val expectedResult = NewsEntity(
            title = "title",
            description = "description",
            content = "url",
            imageUrl = "urlToImage",
            source = "content",
            publishedAt = "publishedAt",
            author = "source",
            url = "url"
        )

        // When
        newsDao.insert(expectedResult)

        // Then
        newsDao.isFavorite(expectedResult.title).test {
            val isFavorite = awaitItem()
            assertThat(isFavorite).isTrue()
        }
    }

    @Test
    fun newsDao_isNotFavorite() = runTest {
        // Given
        val expectedResult = NewsEntity(
            title = "title",
            description = "description",
            content = "url",
            imageUrl = "urlToImage",
            source = "content",
            publishedAt = "publishedAt",
            author = "source",
            url = "url"
        )

        // When
        newsDao.insert(expectedResult)

        // Then
        newsDao.isFavorite("notFavorite").test {
            val isFavorite = awaitItem()
            assertThat(isFavorite).isFalse()
        }
    }

    @Test
    fun newsDao_getAll() = runTest {
        // Given
        val expectedResult = NewsEntity(
            title = "title",
            description = "description",
            content = "url",
            imageUrl = "urlToImage",
            source = "content",
            publishedAt = "publishedAt",
            author = "source",
            url = "url"
        )

        // When
        newsDao.insert(expectedResult)

        // Then
        newsDao.getAll().test {
            val news = awaitItem()
            assertThat(news).isNotEmpty()
        }
    }
}
