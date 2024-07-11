package io.github.joelkanyi.data.network

import io.github.joelkanyi.data.network.dto.NewsResponseDto
import io.github.joelkanyi.data.network.dto.NewsResponseDto.Companion.toNewsDto
import io.github.joelkanyi.domain.model.News

class FakeNewsApi : NewsApi {
    private val news = mutableListOf<NewsResponseDto.ArticleDto>()
    var shouldThrowException = false

    fun addNews(news: List<News>) {
        this.news.addAll(news.map { it.toNewsDto() })
    }

    override suspend fun fetchNews(
        country: String?,
        category: String?,
        pageSize: Int,
        page: Int,
        searchQuery: String?,
        apiKey: String
    ): NewsResponseDto {
        return if (shouldThrowException) {
            throw Exception("Fake exception")
        } else {
            NewsResponseDto(
                articles = news,
                status = "ok",
                totalResults = news.size
            )
        }
    }
}