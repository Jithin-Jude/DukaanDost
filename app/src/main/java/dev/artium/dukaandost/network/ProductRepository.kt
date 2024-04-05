package dev.artium.dukaandost.network

import dev.artium.dukaandost.domain.DataState
import dev.artium.dukaandost.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(): Flow<DataState<List<ProductModel>>>

    suspend fun getAllCategories(): Flow<DataState<List<String>>>
}