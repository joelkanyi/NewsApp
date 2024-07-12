package io.github.joelkanyi.domain.usecase.favorite

import io.github.joelkanyi.domain.model.News
import io.github.joelkanyi.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(news: News) = repository.addFavorite(news)
}
