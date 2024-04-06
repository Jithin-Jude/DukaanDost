package dev.artium.dukaandost.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.artium.dukaandost.DukaanDostConstants.SORT_PRICE_HIGH_TO_HIGH
import dev.artium.dukaandost.DukaanDostConstants.SORT_PRICE_LOW_TO_HIGH
import dev.artium.dukaandost.DukaanDostConstants.SORT_RATING
import dev.artium.dukaandost.DukkanDostUtils.capitalizeFirstLetter
import dev.artium.dukaandost.MainActivity
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.theme.AppBackground
import dev.artium.dukaandost.ui.theme.DividerGrey
import dev.artium.dukaandost.ui.theme.Purple40
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

    if (showFilterOptionBottomSheet) {
        FilterOptionBottomSheet(listOfCategories,
            viewModel.mSelectedFilterList,
            onSelectFilter = { viewModel.refreshProductList(filter = it) },
            onDismiss = {
                showFilterOptionBottomSheet = false
            })
    }

    if (showSortOptionBottomSheet) {
        SortOptionBottomSheet(onSelectOption = {
            viewModel.refreshProductList(sortOption = it)
            showSortOptionBottomSheet = false
        }) {
            showSortOptionBottomSheet = false
        }
    }

    Scaffold(
        Modifier.background(AppBackground)
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
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
            ProductListView(Modifier.weight(1f), listOfProducts, onClickProduct = {
                navController.navigate(MainActivity.Routes.ProductDetailScreen.route + "/${it.id}")
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOptionBottomSheet(
    listOfCategories: List<String>,
    preSelectedFilters: List<String>,
    onSelectFilter: (selectedFilters: List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    LaunchedEffect(Unit) {
        modalBottomSheetState.expand()
    }
    val filtersToShow = mutableListOf<String>()
    filtersToShow.addAll(listOfCategories)

    val selectedFilters = remember { preSelectedFilters.toMutableList() }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        LazyColumn(Modifier.padding(bottom = 70.dp, start = 24.dp, end = 24.dp)) {
            items(filtersToShow) { filter ->
                FilterOptionItem(
                    filter,
                    selectedFilters.contains(filter),
                    onSelectOption = { option, selected ->
                        if (selected) {
                            selectedFilters.add(option)
                        } else {
                            selectedFilters.remove(option)
                        }
                        onSelectFilter(selectedFilters)
                    })
            }
            item {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Clear filter",
                        style = Typography.bodyLarge, color = Purple40,
                        modifier = Modifier.clickable {
                            selectedFilters.clear()
                            onSelectFilter(selectedFilters)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortOptionBottomSheet(
    onSelectOption: (option: String) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val filtersToShow = listOf(SORT_RATING, SORT_PRICE_LOW_TO_HIGH, SORT_PRICE_HIGH_TO_HIGH)
    LaunchedEffect(Unit) {
        modalBottomSheetState.expand()
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        LazyColumn(Modifier.padding(bottom = 70.dp, start = 24.dp, end = 24.dp)) {
            items(filtersToShow) { filter ->
                SortOptionItem(filter, onSelectOption = onSelectOption)
            }
        }
    }
}

@Composable
fun FilterOptionItem(
    option: String,
    selected: Boolean,
    onSelectOption: (option: String, selected: Boolean) -> Unit
) {
    val checkedState = remember { mutableStateOf(selected) }
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                checkedState.value = !checkedState.value
                onSelectOption(option, checkedState.value)
            }
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = option.capitalizeFirstLetter(), style = Typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { isChecked ->
                    checkedState.value = isChecked
                    onSelectOption(option, isChecked)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SortOptionItem(option: String, onSelectOption: (option: String) -> Unit) {
    val checkedState = remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                onSelectOption(option)
            }) {
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = option.capitalizeFirstLetter(), style = Typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { isChecked ->
                    // Update the state when the checkbox is clicked
                    checkedState.value = isChecked
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
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