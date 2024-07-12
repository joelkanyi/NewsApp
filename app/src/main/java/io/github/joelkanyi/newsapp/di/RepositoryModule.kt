package io.github.joelkanyi.newsapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.joelkanyi.data.repository.FavoriteRepositoryImpl
import io.github.joelkanyi.data.repository.NewsRepositoryImpl
import io.github.joelkanyi.domain.repository.FavoriteRepository
import io.github.joelkanyi.domain.repository.NewsRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repository: NewsRepositoryImpl): NewsRepository

    @Binds
    abstract fun bindFavoriteRepository(repository: FavoriteRepositoryImpl): FavoriteRepository
}
