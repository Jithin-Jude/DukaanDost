package dev.artium.dukaandost.ui.feature

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.artium.dukaandost.DukkanDostUtils.appendCurrencyCode
import dev.artium.dukaandost.DukkanDostUtils.capitalizeFirstLetter
import dev.artium.dukaandost.MainActivity
import dev.artium.dukaandost.R
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.components.FilterOptionBottomSheet
import dev.artium.dukaandost.ui.components.SortOptionBottomSheet
import dev.artium.dukaandost.ui.theme.AppBackground
import dev.artium.dukaandost.ui.theme.DividerGrey
import dev.artium.dukaandost.ui.theme.Typography
import dev.artium.dukaandost.viemodel.ProductViewModel


//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    val navController = rememberNavController()
//    DukaanDostTheme {
//        HomeScreenView(navController, false, emptyList(), emptyList())
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenView(
    navController: NavHostController,
    viewModel: ProductViewModel,
    isExpandedScreen: Boolean,
    listOfProducts: List<ProductModel>,
    listOfCategories: List<String>,
) {
    var showFilterOptionBottomSheet by remember { mutableStateOf(false) }
    var showSortOptionBottomSheet by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val scaleFraction = if (state.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(state.progress).coerceIn(0f, 1f)
    if (state.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.clearFiltersAndRefresh()
        }
    }

    if (showFilterOptionBottomSheet) {
        FilterOptionBottomSheet(
            listOfCategories,
            viewModel.mSelectedFilterList,
            onSelectFilter = { viewModel.refreshProductList(filter = it) },
            onDismiss = {
                showFilterOptionBottomSheet = false
            })
    }

    if (showSortOptionBottomSheet) {
        SortOptionBottomSheet(
            viewModel.mSortOption,
            onSelectOption = {
                viewModel.refreshProductList(sortOption = it)
                showSortOptionBottomSheet = false
        }) {
            showSortOptionBottomSheet = false
        }
    }

    Scaffold { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .background(AppBackground)
                .fillMaxSize()
        ) {
            SearchBarWithFilterSort(
                viewModel,
                modifier = Modifier
                    .fillMaxWidth(),
                onSendClick = { value ->
                    viewModel.refreshProductList(searchTerm = value)
                },
                showFilterOptions = {
                    showFilterOptionBottomSheet = true
                },
                showSortOptions = {
                    showSortOptionBottomSheet = true
                },
            )
            Box(
                Modifier
                    .nestedScroll(state.nestedScrollConnection)
                    .weight(1f)) {
                if (isExpandedScreen) {
                    ProductGridView(Modifier.fillMaxSize(), listOfProducts, onClickProduct = {
                        navController.navigate(MainActivity.Routes.ProductDetailScreen.route + "/${it.id}")
                    })
                } else {
                    ProductListView(Modifier.fillMaxSize(), listOfProducts, onClickProduct = {
                        navController.navigate(MainActivity.Routes.ProductDetailScreen.route + "/${it.id}")
                    })
                }
                PullToRefreshContainer(
                    state = state,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction)
                )
            }

        }
    }
}

@Composable
fun ProductGridView(
    modifier: Modifier,
    listOfProducts: List<ProductModel>,
    onClickProduct: (product: ProductModel) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(4)
    ) {
        items(listOfProducts) { product ->
            ProductGridItemView(product, onClickProduct)
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
            ProductListItemView(product, onClickProduct)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(DividerGrey))
        }
    }
}

@Composable
fun ProductGridItemView(product: ProductModel, onClickProduct: (product: ProductModel) -> Unit) {
    Box(
        Modifier
            .padding(8.dp)
            .background(AppBackground)) {
        Column(Modifier.clickable {
            onClickProduct(product)
        }) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                AsyncImage(
                    modifier = Modifier.size(100.dp),
                    model = product.image,
                    contentDescription = null,
                    error = painterResource(R.drawable.ic_placeholed_shopping_bag)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = product.title,
                style = Typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = product.price.toString().appendCurrencyCode(),
                style = Typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = product.rating.rate.toString(), style = Typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = product.category.capitalizeFirstLetter(), style = Typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProductListItemView(product: ProductModel, onClickProduct: (product: ProductModel) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() } // This is mandatory
            ) {
                onClickProduct(product)
            }) {
        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = product.image,
            contentDescription = null,
            error = painterResource(R.drawable.ic_placeholed_shopping_bag)
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
            Text(
                text = product.price.toString().appendCurrencyCode(),
                style = Typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = product.rating.rate.toString(), style = Typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = product.category.capitalizeFirstLetter(), style = Typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SearchBarWithFilterSort(
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit,
    showFilterOptions: () -> Unit,
    showSortOptions: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchTerm = remember { mutableStateOf(viewModel.mSearchTerm) }

    Row(modifier = modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = searchTerm.value,
            onValueChange = { searchTerm.value = it },
            label = { Text("Search") },
            maxLines = 1,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DividerGrey,
                focusedLabelColor = DividerGrey,
                unfocusedBorderColor = DividerGrey,
                unfocusedLabelColor = DividerGrey,
                cursorColor = DividerGrey
            ),
            suffix = {
                if (searchTerm.value.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = DividerGrey,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                searchTerm.value = ""
                                onSendClick(searchTerm.value)
                            }
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                onSendClick(searchTerm.value)
                keyboardController?.hide()
            })
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.List,
            tint = DividerGrey,
            contentDescription = "Filter",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    showFilterOptions()
                }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            tint = DividerGrey,
            contentDescription = "Sort",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    showSortOptions()
                }
        )
    }
}