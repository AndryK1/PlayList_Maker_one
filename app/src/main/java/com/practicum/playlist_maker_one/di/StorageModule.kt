package com.practicum.playlist_maker_one.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlist_maker_one.data.search.dto.SharedPrefsTracksImpl
import com.practicum.playlist_maker_one.data.search.dto.TrackHistoryManagerImpl
import com.practicum.playlist_maker_one.data.settings.SharedPrefsThemeImpl
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack
import com.practicum.playlist_maker_one.domain.api.ThemeManager
import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val storageModule = module {

    factory { Gson() }

    single<TrackHistoryManager> {
        TrackHistoryManagerImpl
    }

    factory<ThemeManager> {
        SharedPrefsThemeImpl(get())
    }

    factory<SharedPrefsTrack> {
        SharedPrefsTracksImpl(get(), get())
    }
}