package dev.artium.dukaandost.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.artium.dukaandost.network.ProductRepository
import dev.artium.dukaandost.network.ProductRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsFeedRepository(impl: ProductRepositoryImpl): ProductRepository

}