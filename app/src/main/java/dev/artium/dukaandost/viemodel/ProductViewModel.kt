package dev.artium.dukaandost.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.artium.dukaandost.DukaanDostConstants
import dev.artium.dukaandost.DukkanDostUtils.printLog
import dev.artium.dukaandost.domain.DataState
import dev.artium.dukaandost.model.ProductCategoryDataModel
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.network.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {
    private val _productCategoryData = MutableLiveData<ProductCategoryDataModel>()
    val productCategoryData: LiveData<ProductCategoryDataModel>
        get() = _productCategoryData

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean>
        get() = _loader

    private var listOfProducts: List<ProductModel> = emptyList()
    private var listOfCategories: List<String> = emptyList()

    init {
        fetchAllProductsAndCategories()
    }

    private fun fetchAllProductsAndCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _loader.postValue(true)
            repository.getAllProducts().collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        printLog("fetchAllProducts Loading")
                        _loader.postValue(true)
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
                        _loader.postValue(true)
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

    fun refreshProductList(filter: String) {
        val productListWithFilter: List<ProductModel>
        if (filter != DukaanDostConstants.FILTER_ALL_CATEGORIES) {
            productListWithFilter = listOfProducts.filter { it.category == filter }
        } else {
            productListWithFilter = listOfProducts
        }

        _productCategoryData.value = ProductCategoryDataModel(
            listOfProducts = productListWithFilter,
            listOfCategories = listOfCategories
        )
    }
}