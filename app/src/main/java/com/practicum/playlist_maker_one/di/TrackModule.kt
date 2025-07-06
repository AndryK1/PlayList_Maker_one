package com.practicum.playlist_maker_one.di

import com.practicum.playlist_maker_one.data.search.dto.TrackRepositoryImpl
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.impl.TrackMapperImpl
import com.practicum.playlist_maker_one.domain.useCase.TrackRepositoryInteractor
import org.koin.dsl.module

val trackModule = module {
    factory<TrackMapper>{
        TrackMapperImpl()
    }

    factory<TrackRepository> {
        TrackRepositoryImpl(
            get(),
            get()
        )
    }

    factory {
        TrackRepositoryInteractor(get())
    }
}