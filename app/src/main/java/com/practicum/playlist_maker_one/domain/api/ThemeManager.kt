package com.practicum.playlist_maker_one.domain.api

import android.content.Context

interface ThemeManager {
    fun setDarkTheme(enabled: Boolean)
    fun isDarkThemeEnabled(): Boolean
}