package dev.artium.dukaandost.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.theme.AppBackground
import dev.artium.dukaandost.ui.theme.DukaanDostTheme
import dev.artium.dukaandost.ui.theme.TextColor


@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    val navController = rememberNavController()
    DukaanDostTheme {
        ProductDetailScreenView(navController, false, "0", emptyList())
    }
}

@Composable
fun ProductDetailScreenView(
    navController: NavHostController,
    isExpandedScreen: Boolean,
    selectedProductId: String,
    listOfProducts: List<ProductModel>
) {
    val selectedProduct = listOfProducts.find { it.id.toString() == selectedProductId }

    Scaffold(
        Modifier.background(AppBackground)
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "value :=> ${selectedProduct?.title}", color = TextColor)
        }
    }
}