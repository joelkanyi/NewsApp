package io.github.joelkanyi.data.cache

import androidx.room.Entity
import io.github.joelkanyi.data.utils.Constants.NEWS_TABLE_NAME
import io.github.joelkanyi.domain.model.News

@Entity(tableName = NEWS_TABLE_NAME, primaryKeys = ["title"])
data class NewsEntity(
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val source: String,
    val publishedAt: String,
    val author: String,
    val url: String,
) {
    companion object {
        fun News.toEntity(): NewsEntity {
            return NewsEntity(
                title = title,
                description = description,
                content = content,
                imageUrl = imageUrl,
                source = source,
                publishedAt = publishedAt,
                author = author,
                url = url,
            )
        }

        fun NewsEntity.toNews(): News {
            return News(
                title = title,
                description = description,
                content = content,
                imageUrl = imageUrl,
                source = source,
                publishedAt = publishedAt,
                author = author,
                url = url,
            )
        }
    }
}
