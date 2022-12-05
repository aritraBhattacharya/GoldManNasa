package com.aritra.goldmannasa.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import javax.inject.Inject

open class NetworkUtils @Inject constructor(private val mContext: Context) {

    open fun getConnectivityStatus(): Boolean {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR))
        ) {
            return true
        }
        return false
    }

    val onlineInterceptor = Interceptor {
        val response = it.proceed(it.request())
        val maxAge = 60
        response.newBuilder()
            .header("Cache-Control", "public, max-age=" + maxAge)
            .removeHeader("Pragma")
            .build()
    }

    val offlineInterceptor = Interceptor {
        val request = it.request()
        if (!getConnectivityStatus()) {
            val maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                .removeHeader("Pragma")
                .build()
        }
        it.proceed(request)
    }

}