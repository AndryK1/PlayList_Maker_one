package com.practicum.playlist_maker_one.data.dto

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker_one.domain.api.SharedPrefs
import com.practicum.playlist_maker_one.domain.api.ThemeManager


class SharedPrefsImpl : SharedPrefs, ThemeManager{
    companion object{
        const val APP_PREFERENCES = "AppPreferences"
        const val DARK_THEME_KEY = "dark_theme"
        const val HISTORY_KEY = "search_history_key"
    }

    override fun saveHistory(history: List<TrackDataDto>, context : Context){
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = Gson().toJson(history)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    override fun getHistory(context : Context) : MutableList<TrackDataDto>{
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = sharedPrefs.getString(HISTORY_KEY, null)
        var list: MutableList<TrackDataDto> = mutableListOf()
        if(json != null)
        {
            val type = object : TypeToken<MutableList<TrackDataDto>>() {}.type
            list = Gson().fromJson(json, type)
            return list
        }
        else{
            return list
        }
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