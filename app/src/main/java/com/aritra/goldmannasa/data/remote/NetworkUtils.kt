package com.aritra.goldmannasa.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

open class NetworkUtils @Inject constructor(private val mContext: Context){

        open fun getConnectivityStatus(): Boolean {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            if(networkCapabilities!=null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))){
                return true
            }
            return false
        }

}