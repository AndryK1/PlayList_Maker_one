package com.practicum.playlist_maker_one.ui.track

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlist_maker_one.domain.api.ThemeManager
import com.practicum.playlist_maker_one.util.Creator


class App : Application(){
    private lateinit var themeManager: ThemeManager

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        themeManager = Creator.getThemeManager()

        AppCompatDelegate.setDefaultNightMode(
            if (themeManager.isDarkThemeEnabled()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }


}