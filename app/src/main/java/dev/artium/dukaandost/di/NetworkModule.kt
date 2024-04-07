package dev.artium.dukaandost.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.artium.dukaandost.DukaanDostConstants.BASE_URL
import dev.artium.dukaandost.network.CacheInterceptor
import dev.artium.dukaandost.network.ForceCacheInterceptor
import dev.artium.dukaandost.network.ProductApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(@ApplicationContext applicationContext: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(File(applicationContext.cacheDir, "http-cache"), 10L * 1024L * 1024L)) // 10 MiB
            .addNetworkInterceptor(CacheInterceptor()) // only if Cache-Control header is not enabled from the server
            .addInterceptor(ForceCacheInterceptor(applicationContext))
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }
}