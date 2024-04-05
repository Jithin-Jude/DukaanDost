package dev.artium.dukaandost.network

import dev.artium.dukaandost.domain.DataState
import dev.artium.dukaandost.model.ProductEntityNetworkMapper
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService,
    private val productEntityNetworkMapper: ProductEntityNetworkMapper
) : ProductRepository {
    override suspend fun getAllProducts() = flow {
        try {
            emit(DataState.Loading)
            val response = productApiService.getAllProducts()
            if (response.isSuccessful) {
                val allProductsResponse = response.body()
                allProductsResponse?.let { allProducts ->
                    val allProductList = allProducts.map {
                        productEntityNetworkMapper.mapFromEntity(it)
                    }
                    emit(DataState.Success(allProductList))
                } ?: kotlin.run {
                    emit(DataState.Error(Exception(response.message())))
                }
            } else {
                emit(DataState.Error(Exception(response.message())))
            }
        } catch (e: Exception) {
            emit(DataState.Error(Exception("error")))
        }
    }
}
