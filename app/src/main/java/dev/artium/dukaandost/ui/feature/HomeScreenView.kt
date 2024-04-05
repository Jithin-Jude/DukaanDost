package dev.artium.dukaandost.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import dev.artium.dukaandost.MainActivity
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.theme.AppBackground
import dev.artium.dukaandost.ui.theme.DividerGrey
import dev.artium.dukaandost.ui.theme.DukaanDostTheme
import dev.artium.dukaandost.ui.theme.Typography


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    DukaanDostTheme {
        HomeScreenView(navController, false, emptyList())
    }
}

@Composable
fun HomeScreenView(
    navController: NavHostController,
    isExpandedScreen: Boolean,
    listOfProducts: List<ProductModel>
) {
    Scaffold(
        Modifier.background(AppBackground)
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchBarWithClear(
                modifier = Modifier
                    .fillMaxWidth(),
                onSendClick = { value ->

                },
            )
            ProductListView(Modifier.weight(1f), listOfProducts, onClickProduct = {
                navController.navigate(MainActivity.Routes.ProductDetailScreen.route + "/${it.id}")
            })
        }
    }
}

@Composable
fun ProductListView(
    modifier: Modifier,
    listOfProducts: List<ProductModel>,
    onClickProduct: (product: ProductModel) -> Unit
) {
    LazyColumn(modifier) {
        items(listOfProducts) { product ->
            ProductItemView(product, onClickProduct)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DividerGrey))
        }
    }
}

@Composable
fun ProductItemView(product: ProductModel, onClickProduct: (product: ProductModel) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClickProduct(product)
            }) {
        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = product.image,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = product.title,
                style = Typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = product.price.toString(), style = Typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = product.rating.rate.toString(), style = Typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = product.category, style = Typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SearchBarWithClear(
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textState = remember { mutableStateOf("") }

    Row(modifier = modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                label = { Text("Search") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DividerGrey,
                    focusedLabelColor = DividerGrey,
                    unfocusedBorderColor = DividerGrey,
                    unfocusedLabelColor = DividerGrey,
                    cursorColor = DividerGrey
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    onSendClick(textState.value)
                    keyboardController?.hide()
                })
            )
            if (textState.value.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = DividerGrey,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                textState.value = ""
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}