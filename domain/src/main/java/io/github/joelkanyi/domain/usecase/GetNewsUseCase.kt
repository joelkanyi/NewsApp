package io.github.joelkanyi.domain.usecase

import io.github.joelkanyi.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(
        country: String? = "us",
        category: String? = null,
    ) = repository.getNews(country, category)
}