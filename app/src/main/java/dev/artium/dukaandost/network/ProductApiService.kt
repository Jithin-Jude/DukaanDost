package dev.artium.dukaandost.network

import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<ProductResponse>>

    @GET("products/categories")
    suspend fun getAllCategories(): Response<List<String>>
}