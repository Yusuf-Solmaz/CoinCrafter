package com.yusuf.cryptocurrencytrading.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    fun showNoInternetDialog(context: Context,finishApp: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("No Internet Connection")
            .setMessage("An internet connection is required to use the application. Please check your internet connection.")
            .setPositiveButton("OK"){
                    dialog, _ ->
                finishApp()
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }
}