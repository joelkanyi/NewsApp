package io.github.joelkanyi.data.network

import io.github.joelkanyi.data.network.dto.NewsResponseDto
import retrofit2.http.GET

interface NewsApi {
    @GET("top-headlines")
    suspend fun fetchNews(
        country: String?,
        category: String?,
        pageSize: Int,
        page: Int,
    ): NewsResponseDto
}