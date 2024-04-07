## [Dukaan Dost: A simple shopping/e-commerce app](https://drive.google.com/file/d/1lzTGRhr5rM8cwTqY0STvRlUtXeJPKFOd/view?usp=sharing "Link to release APK")
A simple shopping/e-commerce app which will list products across different caregories. Users can Search, Filter based on categories, Sort based on different parameters.
App will support phones & tablets above Android Lolipop (SDK 21). App is developed in MVVM architecture.

Notes
--
### Major components of appication:
1. <b>View layer:</b> Responsive UI which will work alike in Phones & Tablets is developed with the help of jetpack compose libraries such as `navigation-compose`, `compose.material3` & 3rd party libs like coil & compose-ratingbar. Responsiveness across different screen sizes & orientation is achived using `Jetpack WindowMetricsCalculator`.

2. <b>Data layer:</b> Data layer of this application is developed by leveraging `OkHttp`, `Retrofit`, `Dagger-Hilt`.

3. <b>State Management:</b> State across app is handled using `ProductViewModel` & State holders in Compose.

### Things implemented to reduce network traffic & improve app performance:
1. <b>OkHttp caching:</b> Once the data is fetched for the first time, app will be able to show data in case of no or poor network conditions. This also helps to reduce network traffic since it getting the cached data. The data will cache for 10 days & this can be adjusted.

2. <b>Caching of image with Coil library & use of placeholders:</b> Use of placeholders enhance UI on loading images for the first time while caching ensure faster image loading for subsequent app sessions.

3. <b>Avoid unnessary API calls:</b> <ins>Since it's able to get all the product details & categories from `https://fakestoreapi.com/products` & `https://fakestoreapi.com/products/categories` respectively, I'm able to reduce the number of API calls and able to make use of already available data. This also helps to achive expected result with less development efforts.</ins>

### Challenges faced during development & solutions arrived:
- App text color has a glitch & was not displaying in one of my POCO C31 device. And app was working fine on all other devices & emulators. The issue was with forced dark theming in Xiaomi/Poco devices. Finally able to resolve it by manipulating `forceDarkAllowed` in app theme.

- App was not performing well in case of poor network (slow loading & data lose). Solved this issue by enabling OkHttp caching.
### Demo phone: [download APK here](https://drive.google.com/file/d/1lzTGRhr5rM8cwTqY0STvRlUtXeJPKFOd/view?usp=sharing "Link to release APK")
![](https://github.com/Jithin-Jude/DukaanDost/blob/0d8ce17989202fae49c2b4d561ec990543216b8d/sample_images/dukaan_dost_phone_gif.gif)
### Demo tablet (emulator):
![](https://github.com/Jithin-Jude/DukaanDost/blob/4e7943cebcafb02742283d58985f66883da7c2ca/sample_images/dukaan_dost_tablet_gif.gif)

### Important Code snippets:
WindowMetricsCalculator:
```kt
val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
```

Filter logic:
```kt
    fun getFilteredList(
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
```

Search logic:
```kt
    fun getProductListWithSearchTerm(
        listOfProducts: List<ProductModel>,
        searchTerm: String
    ): List<ProductModel> {
        val productListContainsSearchTerm: List<ProductModel>
        if (searchTerm.isNotBlank()) {
            productListContainsSearchTerm = listOfProducts.filter {
                it.title.lowercase().contains(searchTerm.lowercase()) ||
                        it.category.lowercase().contains(searchTerm.lowercase())
            }
        } else {
            productListContainsSearchTerm = listOfProducts
        }
        return productListContainsSearchTerm
    }
```

Sort logic:
```kt
fun getSortedProductList(
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
```
### Business logic test coverage:
Unit test was written with the help of the JUnit4 testing framework: `ProductViewModelUnitTest.kt`
![](https://github.com/Jithin-Jude/DukaanDost/blob/f5c6b3aecfb88e7c087e2db689f6e52320ec25d7/sample_images/test_coverage.png)

### Future improvements that can be think of:
The project can be developed into a complete E-commerce app.
1. Login for users: Enable login for users & provide cart, whishlist facility. This can be achive by integration Google auth or OTP via firebase.
2. Paginated data: Pagination can be integrate in get products API call with the help of Paging3 library & serve a large quantity of items.
3. Unit test coverage also can be improved by adding more test case scenarios (success, fail & boundary cases).

<i>Suggestions and improvements are welcome</i>

### 3rd party libraries used:
[Coil for image loading](https://coil-kt.github.io/coil/)

[Compose ratingbar for display rating stars](https://github.com/a914-gowtham/compose-ratingbar)

### References:
[Jetpack WindowMetricsCalculator](https://medium.com/androiddevelopers/jetnews-for-every-screen-4d8e7927752)

[State managemen in compose](https://developer.android.com/develop/ui/compose/state#managing-state)

[Retrieve complex data when navigating](https://developer.android.com/develop/ui/compose/navigation#retrieving-complex-data)

[Pull to refresh](https://developer.android.com/reference/kotlin/androidx/compose/material3/pulltorefresh/package-summary#PullToRefreshContainer(androidx.compose.material3.pulltorefresh.PullToRefreshState,androidx.compose.ui.Modifier,kotlin.Function1,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color))

[Forced OkHttp caching with retrofit](https://amitshekhar.me/blog/caching-with-okhttp-interceptor-and-retrofit#:~:text=can%20create%20a-,ForceCacheInterceptor,-in%20addition%20to)

[Image caching with Coil](https://medium.com/@kamal.lakhani56/coil-image-caching-jetpack-compose-354221918d70)

[Unit Testing](https://developer.android.com/codelabs/basic-android-kotlin-compose-test-viewmodel#3)
