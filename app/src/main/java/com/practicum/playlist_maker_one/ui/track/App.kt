package com.practicum.playlist_maker_one.ui.track

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val APP_PREFERENCES = "AppPreferences"
const val DARK_THEME_KEY = "dark_theme"
const val HISTORY_KEY = "search_history_key"


class App : Application(){

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val isNightModeEnabled = sharedPrefs.getBoolean(DARK_THEME_KEY, false)

        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }


}