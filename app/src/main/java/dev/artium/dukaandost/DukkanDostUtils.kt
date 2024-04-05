package dev.artium.dukaandost

import android.util.Log

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

}