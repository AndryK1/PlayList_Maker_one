package com.practicum.playlist_maker_one.di

import com.practicum.playlist_maker_one.data.converters.TrackDbConverter
import com.practicum.playlist_maker_one.data.db.LikedHistoryRepositoryImpl
import com.practicum.playlist_maker_one.data.search.dto.TrackRepositoryImpl
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.db.LikedHistoryInteractor
import com.practicum.playlist_maker_one.domain.db.LikedHistoryRepository
import com.practicum.playlist_maker_one.domain.impl.LikedHistoryInteractorImpl
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
            get(),
            get()
        )
    }

    factory {
        TrackRepositoryInteractor(get())
    }

    factory {
        TrackDbConverter()
    }

    factory <LikedHistoryRepository>{
        LikedHistoryRepositoryImpl(get(),get())
    }

    factory <LikedHistoryInteractor>{
        LikedHistoryInteractorImpl(get())
    }
}