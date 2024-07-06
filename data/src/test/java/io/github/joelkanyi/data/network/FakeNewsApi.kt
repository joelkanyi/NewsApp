package io.github.joelkanyi.data.network

import io.github.joelkanyi.data.network.dto.NewsResponseDto
import io.github.joelkanyi.data.network.dto.NewsResponseDto.Companion.toNewsDto
import io.github.joelkanyi.domain.model.News

class FakeNewsApi : NewsApi {
    private val news = mutableListOf<NewsResponseDto.ArticleDto>()

    fun addNews(news: List<News>) {
        this.news.addAll(news.map { it.toNewsDto() })
    }

    override suspend fun fetchNews(
        country: String?,
        category: String?,
        pageSize: Int,
        page: Int
    ): NewsResponseDto {
        return NewsResponseDto(
            articles = news,
            status = "ok",
            totalResults = news.size
        )
    }
}