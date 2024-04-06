package dev.artium.dukaandost.model

data class ProductModel(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingModel,
)

data class RatingModel(
    val rate: Double,
    val count: Double,
)