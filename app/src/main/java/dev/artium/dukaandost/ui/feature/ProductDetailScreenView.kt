package dev.artium.dukaandost.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import dev.artium.dukaandost.DukkanDostUtils.appendCurrencyCode
import dev.artium.dukaandost.DukkanDostUtils.networkImageLoaderWithCache
import dev.artium.dukaandost.R
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.components.CategoryLabelView
import dev.artium.dukaandost.ui.components.RatingView
import dev.artium.dukaandost.ui.theme.AppBackground
import dev.artium.dukaandost.ui.theme.DividerGrey
import dev.artium.dukaandost.ui.theme.DukaanDostTheme
import dev.artium.dukaandost.ui.theme.TransparentWhite
import dev.artium.dukaandost.ui.theme.Typography
import kotlinx.coroutines.Dispatchers


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

    Scaffold { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .background(AppBackground)
                .fillMaxSize()
        ) {
            if (isExpandedScreen) {
                ShowProductDetailsSideWise(navController, selectedProduct)
            } else {
                ShowProductDetailsVertical(navController, selectedProduct)
            }
        }
    }
}

@Composable
fun ShowProductDetailsVertical(navController: NavHostController, selectedProduct: ProductModel?) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LazyColumn(
        Modifier
            .fillMaxSize()
    ) {
        item { AppBar(navController) }
        item {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .heightIn(max = screenHeight / 2 - 16.dp),
                model = selectedProduct?.image?.networkImageLoaderWithCache(context = context, R.drawable.ic_placeholed_shopping_bag),
                contentDescription = null,
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
                    style = Typography.bodyMedium,
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
                Text(
                    text = selectedProduct?.price.toString().appendCurrencyCode(),
                    style = Typography.titleLarge
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            RatingView(
                selectedProduct, modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                    text = selectedProduct?.description.toString(),
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
                CategoryLabelView(selectedProduct)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ShowProductDetailsSideWise(navController: NavHostController, selectedProduct: ProductModel?) {
    val context = LocalContext.current

    Box(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                model = selectedProduct?.image?.networkImageLoaderWithCache(context = context, R.drawable.ic_placeholed_shopping_bag),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            LazyColumn(
            ) {
                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = selectedProduct?.title.toString(),
                            style = Typography.bodyMedium,
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
                        Text(
                            text = selectedProduct?.price.toString().appendCurrencyCode(),
                            style = Typography.titleLarge
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    RatingView(
                        selectedProduct, modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
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
                            text = selectedProduct?.description.toString(),
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
                        CategoryLabelView(selectedProduct)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        Box(Modifier.padding(top = 16.dp, start = 16.dp)) {
            Button(
                modifier = Modifier
                    .size(36.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    navController.navigateUp()
                },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TransparentWhite
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = DividerGrey,
                    contentDescription = "Back"
                )
            }
        }
    }
}

@Composable
fun AppBar(navController: NavHostController) {
    Row(Modifier.padding(16.dp)) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            tint = DividerGrey,
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navController.navigateUp()
                }
        )
    }
}