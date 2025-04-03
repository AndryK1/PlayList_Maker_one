package com.practicum.playlist_maker_one.domain.api

import android.content.Context

interface ThemeManager {
    fun setDarkTheme(enabled: Boolean, context: Context)
    fun isDarkThemeEnabled(context: Context): Boolean
}