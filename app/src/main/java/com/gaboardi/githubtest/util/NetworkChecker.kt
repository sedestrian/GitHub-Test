package com.gaboardi.githubtest.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkChecker(val context: Context) {
    fun checkForNetwork(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        cm?.let {
            if (Build.VERSION.SDK_INT < 23) {
                cm.activeNetworkInfo?.let { ni ->
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                }
            } else {
                cm.activeNetwork?.also { network ->
                    cm.getNetworkCapabilities(network)?.also { nc ->
                        return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    }
                }
            }
        }
        return false
    }
}