package com.practicum.playlist_maker_one.data.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.practicum.playlist_maker_one.domain.api.ThemeManager

class SharedPrefsThemeImpl : ThemeManager {
    companion object{
        const val APP_PREFERENCES = "AppPreferences"
        const val DARK_THEME_KEY = "dark_theme"
    }

    override fun setDarkTheme(enabled: Boolean, context: Context) {
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_KEY, enabled)
            .apply()
    }

    override fun isDarkThemeEnabled(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        return sharedPrefs.getBoolean(DARK_THEME_KEY, false)
    }
}