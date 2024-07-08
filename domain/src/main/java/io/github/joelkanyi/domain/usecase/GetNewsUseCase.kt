package io.github.joelkanyi.domain.usecase

import io.github.joelkanyi.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(
        country: String?,
        category: String?,
        searchQuery: String?,
    ) = repository.getNews(
        country = country,
        category = category,
        searchQuery = searchQuery
    )
}