package com.flatcode.simplemultiapps.Wordpress.Util

import android.content.Context
import android.net.ConnectivityManager

object InternetConnection {
    fun checkInternetConnection(context: Context): Boolean {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null
    }
}