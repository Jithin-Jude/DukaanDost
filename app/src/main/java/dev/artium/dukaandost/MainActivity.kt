package dev.artium.dukaandost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.feature.HomeScreenView
import dev.artium.dukaandost.ui.feature.ProductDetailScreenView
import dev.artium.dukaandost.ui.theme.DukaanDostTheme
import dev.artium.dukaandost.viemodel.ProductViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { }
        observeData()
        viewModel.fetchAllProducts()
//        viewModel.fetchAllCategories()
    }

    private fun observeData() {
        viewModel.listOfProducts.observe(this) { productList ->
            setContent {
                val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
                val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
                val navController = rememberNavController()
                DukaanDostTheme {
                    DashboardView(navController, isExpandedScreen, productList ?: emptyList())
                }
            }
        }
    }

    @Composable
    fun DashboardView(
        navController: NavHostController,
        isExpandedScreen: Boolean,
        listOfProducts: List<ProductModel>
    ) {
        NavHost(navController, startDestination = Routes.HomeScreen.route) {
            composable(Routes.HomeScreen.route) {
                HomeScreenView(
                    navController,
                    isExpandedScreen,
                    listOfProducts
                )
            }
            composable(Routes.ProductDetailScreen.route + "/{product_id}") { navBackStack ->
                val selectedProductId = navBackStack.arguments?.getString("product_id")
                selectedProductId?.let { id ->
                    ProductDetailScreenView(
                        navController,
                        isExpandedScreen,
                        id
                    )
                }
            }
        }
    }

    sealed class Routes(val route: String) {
        object HomeScreen : Routes("homeScreen")
        object ProductDetailScreen : Routes("productDetailScreen")
    }
}