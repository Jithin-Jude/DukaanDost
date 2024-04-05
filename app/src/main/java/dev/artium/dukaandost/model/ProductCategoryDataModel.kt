package dev.artium.dukaandost.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProductCategoryDataModel(
    val listOfProducts: List<ProductModel>,
    val listOfCategories: List<String>,
)