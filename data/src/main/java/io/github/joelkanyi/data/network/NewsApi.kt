package io.github.joelkanyi.data.network

import io.github.joelkanyi.data.BuildConfig
import io.github.joelkanyi.data.network.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun fetchNews(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ): NewsResponseDto
}