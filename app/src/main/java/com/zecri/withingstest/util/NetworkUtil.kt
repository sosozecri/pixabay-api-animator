package com.zecri.withingstest.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import retrofit2.HttpException
import java.io.IOException

private const val HTTP_BAD_REQUEST_CODE = 400
private const val HTTP_TOO_MANY_REQUEST_CODE = 429

/**
 * Exception describing a situation where the network is not able to reach the internet
 */
class NoConnectivityException(message: String? = null) : IOException(message)

fun HttpException.isBadRequest() = code() == HTTP_BAD_REQUEST_CODE

/**
 * Return true if the network is able to reach the internet, false otherwise
 */
fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) { //NetworkInfo is deprecated in API 29
        connectivityManager.isOnline()
    } else {
        connectivityManager.isOnlineLegacy()
    }
}

/**
 * Return true if active network info is connected or connecting, false otherwise
 * NetworkInfo is deprecated in API 29
 */
fun ConnectivityManager.isOnlineLegacy(): Boolean {
    return activeNetworkInfo?.isConnectedOrConnecting == true
}

/**
 * Return true if the network is able to reach the internet, false otherwise
 */
@RequiresApi(Build.VERSION_CODES.M)
fun ConnectivityManager.isOnline(): Boolean {
    val networkCapabilities: NetworkCapabilities? = getNetworkCapabilities(activeNetwork)
    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

fun Context.openUrl(url: String, offlineExecutable: (() -> Unit)? = null) {
    if (!isOnline()) {
        offlineExecutable?.invoke()
        return
    }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

