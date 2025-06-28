package com.practicum.playlist_maker_one.ui.track

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

import com.practicum.playlist_maker_one.di.networkModule
import com.practicum.playlist_maker_one.di.playerModule
import com.practicum.playlist_maker_one.di.storageModule
import com.practicum.playlist_maker_one.di.trackModule
import com.practicum.playlist_maker_one.di.viewModelsModule
import com.practicum.playlist_maker_one.domain.api.ThemeManager
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class App : Application(){
    private val themeManager: ThemeManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@App)

            modules( playerModule, networkModule, storageModule, trackModule,
                viewModelsModule)
        }

        AppCompatDelegate.setDefaultNightMode(
            if (themeManager.isDarkThemeEnabled()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }


}