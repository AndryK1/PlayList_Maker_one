package com.practicum.playlist_maker_one.di

import com.practicum.playlist_maker_one.data.converters.PlayListDbConverter
import com.practicum.playlist_maker_one.data.converters.TrackDbConverter
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.impl.TrackMapperImpl
import org.koin.dsl.module

val converters = module{

    factory<TrackMapper>{
        TrackMapperImpl()
    }

    factory {
        TrackDbConverter()
    }

    factory {
        PlayListDbConverter()
    }
}