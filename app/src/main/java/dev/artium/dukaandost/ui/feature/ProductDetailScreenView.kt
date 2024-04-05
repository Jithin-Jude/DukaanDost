package dev.artium.dukaandost.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import dev.artium.dukaandost.R
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.theme.AppBackground
import dev.artium.dukaandost.ui.theme.DukaanDostTheme
import dev.artium.dukaandost.ui.theme.PureBlack
import dev.artium.dukaandost.ui.theme.Typography


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
    val selectedProduct = remember {
        listOfProducts.find { it.id.toString() == selectedProductId }
    }
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        Modifier.background(AppBackground),
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item { AppBar(navController) }
            item {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .heightIn(max = screenHeight / 2 - 16.dp),
                    model = selectedProduct?.image,
                    contentDescription = null,
                    error = painterResource(R.drawable.ic_launcher_background)
                )
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = selectedProduct?.title.toString(),
                        style = Typography.bodyLarge,
                    )
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = selectedProduct?.price.toString(), style = Typography.titleLarge)
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = selectedProduct?.rating?.rate.toString(),
                        style = Typography.bodyLarge
                    )
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = selectedProduct?.category.toString(), style = Typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AppBar(navController: NavHostController) {
    Row(Modifier.padding(16.dp)) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            tint = PureBlack,
            contentDescription = "Back",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    navController.navigateUp()
                }
        )
    }
}