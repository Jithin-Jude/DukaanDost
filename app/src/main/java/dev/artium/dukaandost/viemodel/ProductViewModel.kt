package dev.artium.dukaandost.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.artium.dukaandost.DukkanDostUtils.printLog
import dev.artium.dukaandost.domain.DataState
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.network.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {

    private val _listOfProducts = MutableLiveData<List<ProductModel>?>()
    val listOfProducts: LiveData<List<ProductModel>?>
        get() = _listOfProducts

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean>
        get() = _loader

    fun fetchAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllProducts().collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        printLog("Loading")
                        _loader.postValue(true)
                    }

                    is DataState.Success -> {
                        printLog("Success: ${result.data}")
                        _listOfProducts.postValue(result.data)
                    }

                    is DataState.Error -> {
                        printLog("Error")
                        _listOfProducts.postValue(null)
                    }
                }
            }
        }
    }
}