package dev.artium.dukaandost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.feature.HomeScreenView
import dev.artium.dukaandost.ui.feature.ProductDetailScreenView
import dev.artium.dukaandost.ui.theme.AppBackground
import dev.artium.dukaandost.ui.theme.DividerGrey
import dev.artium.dukaandost.ui.theme.DukaanDostTheme
import dev.artium.dukaandost.viemodel.ProductViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {}
        observeData()
    }

    private fun observeData() {
        viewModel.productCategoryData.observe(this) { data ->
            setContent {
                val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
                val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
                val navController = rememberNavController()
                DukaanDostTheme {
                    if (data?.listOfProducts.isNullOrEmpty()) {
                        EmptyScreenView(viewModel)
                    } else {
                        DashboardView(
                            navController,
                            viewModel,
                            isExpandedScreen,
                            data?.listOfProducts ?: emptyList(),
                            data?.listOfCategories ?: emptyList(),
                        )
                    }
                }
            }
        }
        viewModel.loader.observe(this) {
            if (it) {
                setContent {
                    LoadingView()
                }
            }
        }
    }

    @Composable
    fun DashboardView(
        navController: NavHostController,
        viewModel: ProductViewModel,
        isExpandedScreen: Boolean,
        listOfProducts: List<ProductModel>,
        listOfCategories: List<String>,
    ) {
        NavHost(navController, startDestination = Routes.HomeScreen.route) {
            composable(Routes.HomeScreen.route) {
                HomeScreenView(
                    navController,
                    viewModel,
                    isExpandedScreen,
                    listOfProducts,
                    listOfCategories
                )
            }
            composable(Routes.ProductDetailScreen.route + "/{product_id}") { navBackStack ->
                val selectedProductId = navBackStack.arguments?.getString("product_id")
                selectedProductId?.let { id ->
                    ProductDetailScreenView(
                        navController,
                        isExpandedScreen,
                        id,
                        listOfProducts
                    )
                }
            }
        }
    }

    @Composable
    fun LoadingView() {
        Box(
            Modifier
                .fillMaxSize()
                .background(AppBackground), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 3.dp
            )
        }
    }

    @Composable
    fun EmptyScreenView(viewModel: ProductViewModel) {
        Box(
            Modifier
                .fillMaxSize()
                .background(AppBackground), contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Outlined.Warning,
                tint = DividerGrey,
                contentDescription = "Refresh",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        viewModel.fetchAllProductsAndCategories()
                    })
        }
    }

    sealed class Routes(val route: String) {
        object HomeScreen : Routes("homeScreen")
        object ProductDetailScreen : Routes("productDetailScreen")
    }
}