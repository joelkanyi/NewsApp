package io.github.joelkanyi.presentation.model

import android.os.Parcelable
import io.github.joelkanyi.domain.model.News
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class NewsUiModel(
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val source: String,
    val publishedAt: String,
    val author: String,
    val url: String,
) : Parcelable {
    companion object {
        fun News.toUiModel(): NewsUiModel {
            return NewsUiModel(
                author = author,
                content = content,
                description = description,
                publishedAt = publishedAt,
                source = source,
                title = title,
                url = url,
                imageUrl = imageUrl,
            )
        }
    }
}
