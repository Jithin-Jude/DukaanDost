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
        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
            val navController = rememberNavController()
            DukaanDostTheme {
                DashboardView(navController, isExpandedScreen)
            }
        }
//        viewModel.fetchAllProducts()
//        viewModel.fetchAllCategories()
    }

    @Composable
    fun DashboardView(navController: NavHostController, isExpandedScreen: Boolean) {
        NavHost(navController, startDestination = "homeScreen") {
            composable("homeScreen") { HomeScreenView(navController, isExpandedScreen) }
            composable("productDetailScreen") {
                ProductDetailScreenView(
                    navController,
                    isExpandedScreen
                )
            }
        }
    }
}