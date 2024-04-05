package dev.artium.dukaandost.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductModel(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingModel,
) : Parcelable

@Parcelize
data class RatingModel(
    val rate: Double,
    val count: Double,
) : Parcelable