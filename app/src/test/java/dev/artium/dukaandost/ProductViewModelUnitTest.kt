package dev.artium.dukaandost

import androidx.activity.viewModels
import androidx.lifecycle.SavedStateHandle
import dev.artium.dukaandost.domain.ProductEntityNetworkMapper
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.model.RatingModel
import dev.artium.dukaandost.network.ProductApiService
import dev.artium.dukaandost.network.ProductRepository
import dev.artium.dukaandost.network.ProductRepositoryImpl
import dev.artium.dukaandost.viemodel.ProductViewModel
import okhttp3.OkHttpClient
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ProductViewModelUnitTest {
    private val okHttpClient = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(DukaanDostConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val productApiService = retrofit.create(ProductApiService::class.java)
    private val productEntityNetworkMapper = ProductEntityNetworkMapper()
    private val savedStateHandle = SavedStateHandle()
    private val repo = ProductRepositoryImpl(
        productApiService = productApiService,
        productEntityNetworkMapper = productEntityNetworkMapper
    )
    private val viewModel = ProductViewModel(repository = repo, savedStateHandle = savedStateHandle)
    @Test
    fun productViewModel_GetFilteredList_NewListHasProductsFromFiltersOnly() {
        // Mock data
        val listOfProducts = listOf(
            ProductModel(
                1,
                "Product 1",
                10.0,
                "Description 1",
                "Category A",
                "",
                RatingModel(4.5, 100.0)
            ),
            ProductModel(
                2,
                "Product 2",
                20.0,
                "Description 2",
                "Category B",
                "",
                RatingModel(4.0, 200.0)
            ),
            ProductModel(
                3,
                "Product 3",
                30.0,
                "Description 3",
                "Category C",
                "",
                RatingModel(3.5, 150.0)
            ),
            ProductModel(
                4,
                "Product 4",
                40.0,
                "Description 4",
                "Category D",
                "",
                RatingModel(4.8, 180.0)
            )
        )
        val filters = listOf("Category A", "Category B")

        // Call the method under test
        val filteredList = viewModel.getFilteredList(listOfProducts, filters)

        // Expected result: Only products with categories "Category A" and "Category B" should be in the filtered list
        val expectedFilteredList = listOf(
            ProductModel(
                1,
                "Product 1",
                10.0,
                "Description 1",
                "Category A",
                "",
                RatingModel(4.5, 100.0)
            ),
            ProductModel(
                2,
                "Product 2",
                20.0,
                "Description 2",
                "Category B",
                "",
                RatingModel(4.0, 200.0)
            )
        )

        // Assert that the filtered list matches the expected result
        assertEquals(expectedFilteredList, filteredList)
    }
}