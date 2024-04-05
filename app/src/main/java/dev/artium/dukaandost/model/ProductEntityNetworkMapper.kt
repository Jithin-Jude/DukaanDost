package dev.artium.dukaandost.model

import dev.artium.dukaandost.domain.EntityMapper
import dev.artium.dukaandost.network.ProductResponse
import javax.inject.Inject

class ProductEntityNetworkMapper @Inject constructor() :
    EntityMapper<ProductResponse, ProductModel> {
    override fun mapFromEntity(entity: ProductResponse): ProductModel {

        return ProductModel(
            id = entity.id,
            title = entity.title,
            price = entity.price,
            description = entity.description,
            category = entity.category,
            image = entity.image,
            rating = RatingModel(
                rate = entity.rating.rate,
                count = entity.rating.count
            ),
        )
    }

    override fun mapToEntity(model: ProductModel): ProductResponse {
        TODO("Not yet implemented")
    }
}