package com.practicum.playlist_maker_one.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.practicum.playlist_maker_one.R

class InternetBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return


        if(intent?.action == ACTION){
            if(!isInternetAvailable(context)){
                Toast.makeText(context, context.getString(R.string.noInternetToast) , Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isInternetAvailable(context: Context) : Boolean{

        // системный сервис для работы с сетевыми подключениями
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Получаем активную сеть (Wi-Fi, мобильные данные и т.д.)
        val network = connectivityManager.activeNetwork ?: return false
        // Получаем возможности активной сети
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    companion object{
        const val ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}