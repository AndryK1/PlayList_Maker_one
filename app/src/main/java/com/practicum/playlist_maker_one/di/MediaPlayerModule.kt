package com.practicum.playlist_maker_one.di

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlist_maker_one.data.player.dto.TrackPlayerImpl
import com.practicum.playlist_maker_one.domain.api.TrackPlayer
import org.koin.dsl.module

val playerModule = module {

    factory {
        MediaPlayer()
    }

    single {
        Handler(Looper.getMainLooper())
    }

    factory<TrackPlayer> {
        TrackPlayerImpl(
            get()
        )
    }
}