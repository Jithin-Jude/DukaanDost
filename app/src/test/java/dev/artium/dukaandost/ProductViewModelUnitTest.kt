package dev.artium.dukaandost

import androidx.lifecycle.SavedStateHandle
import dev.artium.dukaandost.DukaanDostConstants.SORT_RATING
import dev.artium.dukaandost.domain.ProductEntityNetworkMapper
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.model.RatingModel
import dev.artium.dukaandost.network.ProductApiService
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
    private val repo = ProductRepositoryImpl(productApiService = productApiService, productEntityNetworkMapper = productEntityNetworkMapper)
    private val viewModel = ProductViewModel(repository = repo, savedStateHandle = savedStateHandle)

    // getFilteredList SUCCESS TEST CASE
    @Test
    fun productViewModel_GetFilteredList_NewListHasProductsFromFiltersOnly(){
        // Mock data
        val listOfProducts = listOf(
            ProductModel(1, "Product 1", 10.0, "Description 1", "Category A", "", RatingModel(4.5, 100.0)),
            ProductModel(2, "Product 2", 20.0, "Description 2", "Category B", "", RatingModel(4.0, 200.0)),
            ProductModel(3, "Product 3", 30.0, "Description 3", "Category C", "", RatingModel(3.5, 150.0)),
            ProductModel(4, "Product 4", 40.0, "Description 4", "Category D", "", RatingModel(4.8, 180.0))
        )
        val filters = listOf("Category A", "Category B")

        // Call the method under test
        val filteredList = viewModel.getFilteredList(listOfProducts, filters)

        // Expected result: Only products with categories "Category A" and "Category B" should be in the filtered list
        val expectedFilteredList = listOf(
            ProductModel(1, "Product 1", 10.0, "Description 1", "Category A", "", RatingModel(4.5, 100.0)),
            ProductModel(2, "Product 2", 20.0, "Description 2", "Category B", "", RatingModel(4.0, 200.0))
        )

        // Assert that the filtered list matches the expected result
        assertEquals(expectedFilteredList, filteredList)
    }

    // getFilteredList FAIL TEST CASE
    @Test
    fun productViewModel_GetFilteredList_NewListHasProductsFromFiltersAndOthers() {
        // Mock data
        val listOfProducts = listOf(
            ProductModel(1, "Product 1", 10.0, "Description 1", "Category A", "", RatingModel(4.5, 100.0)),
            ProductModel(2, "Product 2", 20.0, "Description 2", "Category B", "", RatingModel(4.0, 200.0)),
            ProductModel(3, "Product 3", 30.0, "Description 3", "Category C", "", RatingModel(3.5, 150.0)),
            ProductModel(4, "Product 4", 40.0, "Description 4", "Category D", "", RatingModel(4.8, 180.0))
        )
        val filters = listOf("Category A", "Category B")

        // Call the method under test
        val filteredList = viewModel.getFilteredList(listOfProducts, filters)

        // Check that the filtered list contains products from both filters and others
        assertFalse(filteredList.all { it.category in filters })
        assertFalse(filteredList.all { it.category !in filters })
    }


    // getFilteredList BOUNDARY TEST CASE
    @Test
    fun productViewModel_GetFilteredList_NewListBoundaryCase() {
        // Boundary case 1: Empty list of products
        val emptyListOfProducts = emptyList<ProductModel>()
        val filters1 = listOf("Category A", "Category B")
        val filteredList1 = viewModel.getFilteredList(emptyListOfProducts, filters1)
        assertEquals(emptyListOfProducts, filteredList1)

        // Boundary case 2: Empty list of filters
        val listOfProducts = listOf(
            ProductModel(1, "Product 1", 10.0, "Description 1", "Category A", "", RatingModel(4.5, 100.0)),
            ProductModel(2, "Product 2", 20.0, "Description 2", "Category B", "", RatingModel(4.0, 200.0)),
            ProductModel(3, "Product 3", 30.0, "Description 3", "Category C", "", RatingModel(3.5, 150.0)),
            ProductModel(4, "Product 4", 40.0, "Description 4", "Category D", "", RatingModel(4.8, 180.0))
        )
        val emptyListOfFilters = emptyList<String>()
        val filteredList2 = viewModel.getFilteredList(listOfProducts, emptyListOfFilters)
        assertEquals(listOfProducts, filteredList2)
    }

    // getProductListWithSearchTerm SUCCESS TEST CASE
    @Test
    fun productViewModel_GetProductListWithSearchTerm_NewListShouldContainProductsWithSearchTermOnly() {
        // Mock data
        val listOfProducts = listOf(
            ProductModel(1, "Product 1", 10.0, "Description 1", "Category A", "", RatingModel(4.5, 100.0)),
            ProductModel(2, "Product 2", 20.0, "Description 2", "Category B", "", RatingModel(4.0, 200.0)),
            ProductModel(3, "Product 3", 30.0, "Description 3", "Category C", "", RatingModel(3.5, 150.0)),
            ProductModel(4, "Product 4", 40.0, "Description 4", "Category D", "", RatingModel(4.8, 180.0))
        )
        val searchTerm = "Product" // Assuming 'Product' as the search term

        // Call the method under test
        val filteredList = viewModel.getProductListWithSearchTerm(listOfProducts, searchTerm)

        // Assert that the filtered list does not contain products with the search term in title or category
        assertTrue(filteredList.all { it.title.contains(searchTerm, ignoreCase = true) && it.category.contains(searchTerm, ignoreCase = true) })
    }

    // getProductListWithSearchTerm FAIL TEST CASE
    @Test
    fun productViewModel_GetProductListWithSearchTerm_NewListShouldNotContainProductsWithSearchTerm() {
        // Mock data
        val listOfProducts = listOf(
            ProductModel(1, "Product 1", 10.0, "Description 1", "Category A", "", RatingModel(4.5, 100.0)),
            ProductModel(2, "Product 2", 20.0, "Description 2", "Category B", "", RatingModel(4.0, 200.0)),
            ProductModel(3, "Product 3", 30.0, "Description 3", "Category C", "", RatingModel(3.5, 150.0)),
            ProductModel(4, "Product 4", 40.0, "Description 4", "Category D", "", RatingModel(4.8, 180.0))
        )
        val searchTerm = "Element" // Assuming 'product' as the search term

        // Call the method under test
        val filteredList = viewModel.getProductListWithSearchTerm(listOfProducts, searchTerm)

        assertTrue(filteredList.isNotEmpty())
    }

    // getSortedProductList SUCCESS TEST CASE
    @Test
    fun productViewModel_GetSortedProductList_NewListShouldContainsListOfProductsSortedBasedOnRating() {
        // Mock data
        val listOfProducts = listOf(
            ProductModel(1, "Product 1", 10.0, "Description 1", "Category A", "", RatingModel(4.5, 100.0)),
            ProductModel(2, "Product 2", 20.0, "Description 2", "Category B", "", RatingModel(4.0, 200.0)),
            ProductModel(3, "Product 3", 30.0, "Description 3", "Category C", "", RatingModel(3.5, 150.0)),
            ProductModel(4, "Product 4", 40.0, "Description 4", "Category D", "", RatingModel(4.8, 180.0))
        )
        val sortOption = SORT_RATING

        // Call the method under test
        val sortedList = viewModel.getSortedProductList(listOfProducts, sortOption)

        // Assert that the sorted list contains products sorted based on rating in descending order
        val expectedList = listOfProducts.sortedByDescending { it.rating.rate }
        assertEquals(expectedList, sortedList)
    }

    // getSortedProductList FAIL TEST CASE
    @Test
    fun productViewModel_GetSortedProductList_NewListContainsListOfProductsNotSortedBasedOnRating() {
        // Mock data
        val listOfProducts = listOf(
            ProductModel(1, "Product 1", 10.0, "Description 1", "Category A", "", RatingModel(4.5, 100.0)),
            ProductModel(2, "Product 2", 20.0, "Description 2", "Category B", "", RatingModel(4.0, 200.0)),
            ProductModel(3, "Product 3", 30.0, "Description 3", "Category C", "", RatingModel(3.5, 150.0)),
            ProductModel(4, "Product 4", 40.0, "Description 4", "Category D", "", RatingModel(4.8, 180.0))
        )
        val sortOption = SORT_RATING

        // Call the method under test
        val sortedList = viewModel.getSortedProductList(listOfProducts, sortOption)

        // Assertion that the sorted list contains products sorted based on price
        val expectedList = listOfProducts.sortedBy { it.price }

        // Deliberately fail the test by asserting the opposite
        assertEquals(expectedList, sortedList)
    }

    // clearFiltersAndRefresh SUCCESS TEST CASE
    @Test
    fun productViewModel_ClearFiltersAndRefresh_NewValueOfVariablesShouldBeEmptyOrDefault() {

        // Call the method under test
        viewModel.clearFiltersAndRefresh()

        // Assert that the search term is empty
        assertEquals("", viewModel.mSearchTerm)

        // Assert that the selected filter list is empty
        assertTrue(viewModel.mSelectedFilterList.isEmpty())

        // Assert that the sort option is default
        assertEquals(DukaanDostConstants.SORT_DEFAULT, viewModel.mSortOption)
    }

    // clearFiltersAndRefresh FAIL TEST CASE
    @Test
    fun productViewModel_ClearFiltersAndRefresh_NewValueOfVariablesShouldNotBeEmptyOrDefault() {

        // Call the method under test
        viewModel.clearFiltersAndRefresh()

        // The values are not default or empty
        viewModel.mSearchTerm = "test"
        viewModel.mSelectedFilterList = listOf("filter1", "filter2")
        viewModel.mSortOption = "sortOption"

        // Assert that the search term is empty (This assertion should fail)
        assertEquals("", viewModel.mSearchTerm)

        // Assert that the selected filter list is empty (This assertion should fail)
        assertTrue(viewModel.mSelectedFilterList.isEmpty())

        // Assert that the sort option is default (This assertion should fail)
        assertEquals(DukaanDostConstants.SORT_DEFAULT, viewModel.mSortOption)
    }
}