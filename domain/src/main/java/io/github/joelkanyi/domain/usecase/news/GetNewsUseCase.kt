package io.github.joelkanyi.domain.usecase.news

import io.github.joelkanyi.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(
        country: String?,
        category: String?
    ) = repository.getNews(
        country = country,
        category = category,
        searchQuery = null
    )
}
