package io.github.joelkanyi.domain.usecase.favorite

import io.github.joelkanyi.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoritesUseCase
    @Inject
    constructor(
        private val repository: FavoriteRepository,
    ) {
        operator fun invoke() = repository.getFavorites()
    }
