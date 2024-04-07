package dev.artium.dukaandost

import android.content.Context
import android.util.Log
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers

object DukkanDostUtils {
    fun printLog(text: String?) {
        Log.d("DUKAAN_DOST", "DEBUG :=> ${text}")
    }
    fun String.capitalizeFirstLetter(): String {
        return if (isEmpty()) {
            this
        } else {
            substring(0, 1).uppercase() + substring(1)
        }
    }
    fun String.appendCurrencyCode(): String {
        return "$$this"
    }
    fun String.networkImageLoaderWithCache(context: Context, placeholder: Int): ImageRequest {
        return ImageRequest.Builder(context)
            .data(this)
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(this)
            .diskCacheKey(this)
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}