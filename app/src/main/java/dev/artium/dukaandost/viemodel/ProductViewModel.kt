package dev.artium.dukaandost.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.artium.dukaandost.DukaanDostConstants
import dev.artium.dukaandost.DukaanDostConstants.SORT_PRICE_HIGH_TO_HIGH
import dev.artium.dukaandost.DukaanDostConstants.SORT_PRICE_LOW_TO_HIGH
import dev.artium.dukaandost.DukaanDostConstants.SORT_RATING
import dev.artium.dukaandost.DukkanDostUtils.printLog
import dev.artium.dukaandost.domain.DataState
import dev.artium.dukaandost.model.ProductCategoryDataModel
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.network.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val FILTER_KEY = "selected_filter"
        private const val SEARCH_TERM = "search_term"
        private const val SORT_OPTION = "sort_option"
    }

    var mSelectedFilterList: List<String>
        get() = savedStateHandle[FILTER_KEY] ?: listOf()
        set(value) {
            savedStateHandle[FILTER_KEY] = value
        }
    var mSearchTerm: String
        get() = savedStateHandle[SEARCH_TERM] ?: ""
        set(value) {
            savedStateHandle[SEARCH_TERM] = value
        }
    var mSortOption: String
        get() = savedStateHandle[SORT_OPTION] ?: DukaanDostConstants.SORT_DEFAULT
        set(value) {
            savedStateHandle[SORT_OPTION] = value
        }

    private val _productCategoryData = MutableLiveData<ProductCategoryDataModel>()
    val productCategoryData: LiveData<ProductCategoryDataModel>
        get() = _productCategoryData

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean>
        get() = _loader

    private var listOfProducts: List<ProductModel> = emptyList()
    private var listOfCategories: List<String> = emptyList()

    init {
        if (!savedStateHandle.contains(FILTER_KEY)) {
            savedStateHandle[FILTER_KEY] = listOf<String>()
        }
        if (!savedStateHandle.contains(SEARCH_TERM)) {
            savedStateHandle[SEARCH_TERM] = ""
        }
        if (!savedStateHandle.contains(SORT_OPTION)) {
            savedStateHandle[SORT_OPTION] = DukaanDostConstants.SORT_DEFAULT
        }
        fetchAllProductsAndCategories()
    }

    fun clearFiltersAndRefresh() {
        mSearchTerm = ""
        mSelectedFilterList = listOf()
        mSortOption = DukaanDostConstants.SORT_DEFAULT
        fetchAllProductsAndCategories()
    }

    fun fetchAllProductsAndCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _loader.postValue(true)
            repository.getAllProducts().collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        printLog("fetchAllProducts Loading")
                    }

                    is DataState.Success -> {
                        printLog("fetchAllProducts Success: ${result.data}")
                        listOfProducts = result.data
                    }

                    is DataState.Error -> {
                        printLog("fetchAllProducts Error")
                        listOfProducts = emptyList()
                    }
                }
            }
            repository.getAllCategories().collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        printLog("fetchAllCategories Loading")
                    }

                    is DataState.Success -> {
                        printLog("fetchAllCategories Success: ${result.data}")
                        listOfCategories = result.data
                    }

                    is DataState.Error -> {
                        printLog("fetchAllCategories Error")
                        listOfCategories = emptyList()
                    }
                }
            }
            _productCategoryData.postValue(
                ProductCategoryDataModel(
                    listOfProducts = listOfProducts,
                    listOfCategories = listOfCategories
                )
            )
            _loader.postValue(false)
        }
    }

    fun refreshProductList(
        searchTerm: String = mSearchTerm,
        filter: List<String> = mSelectedFilterList,
        sortOption: String = mSortOption,
    ) {
        mSearchTerm = searchTerm
        mSelectedFilterList = filter
        mSortOption = sortOption

        val productListWithFilter = getFilteredList(listOfProducts, mSelectedFilterList)

        val productListContainsSearchTerm =
            getProductListWithSearchTerm(listOfProducts, mSearchTerm)

        val intersectionList =
            productListWithFilter.intersect(productListContainsSearchTerm.toSet()).toList()

        val sortedIntersectionList = getSortedProductList(intersectionList, mSortOption)

        _productCategoryData.value = ProductCategoryDataModel(
            listOfProducts = sortedIntersectionList,
            listOfCategories = listOfCategories
        )
    }

    private fun getFilteredList(
        listOfProducts: List<ProductModel>,
        filters: List<String>
    ): List<ProductModel> {
        val productListWithFilter: List<ProductModel>
        if (filters.isNotEmpty()) {
            productListWithFilter = listOfProducts.filter { product ->
                filters.any { filter ->
                    product.category == filter
                }
            }
        } else {
            productListWithFilter = listOfProducts
        }
        return productListWithFilter
    }

    private fun getProductListWithSearchTerm(
        listOfProducts: List<ProductModel>,
        searchTerm: String
    ): List<ProductModel> {
        val productListContainsSearchTerm: List<ProductModel>
        if (searchTerm.isNotBlank()) {
            productListContainsSearchTerm = listOfProducts.filter {
                it.title.lowercase().contains(searchTerm) ||
                        it.category.lowercase().contains(searchTerm)
            }
        } else {
            productListContainsSearchTerm = listOfProducts
        }
        return productListContainsSearchTerm
    }

    private fun getSortedProductList(
        listOfProducts: List<ProductModel>,
        sortOption: String
    ): List<ProductModel> {
        val sortedIntersectionList: List<ProductModel>
        when (sortOption) {
            SORT_RATING -> {
                sortedIntersectionList = listOfProducts.sortedByDescending { it.rating.rate }
            }

            SORT_PRICE_LOW_TO_HIGH -> {
                sortedIntersectionList = listOfProducts.sortedBy { it.price }
            }

            SORT_PRICE_HIGH_TO_HIGH -> {
                sortedIntersectionList = listOfProducts.sortedByDescending { it.price }
            }

            else -> {
                sortedIntersectionList = listOfProducts
            }
        }
        return sortedIntersectionList
    }
}