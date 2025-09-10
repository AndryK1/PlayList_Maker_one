package com.practicum.playlist_maker_one.di

import com.practicum.playlist_maker_one.data.converters.TrackDbConverter
import com.practicum.playlist_maker_one.data.db.LikedHistoryRepositoryImpl
import com.practicum.playlist_maker_one.data.db.PlayListRepositoryImpl
import com.practicum.playlist_maker_one.data.search.dto.TrackRepositoryImpl
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.db.LikedHistoryInteractor
import com.practicum.playlist_maker_one.domain.db.LikedHistoryRepository
import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.db.PlayListRepository
import com.practicum.playlist_maker_one.domain.impl.LikedHistoryInteractorImpl
import com.practicum.playlist_maker_one.domain.impl.PlayListInteractorImpl
import com.practicum.playlist_maker_one.domain.impl.TrackMapperImpl
import com.practicum.playlist_maker_one.domain.useCase.TrackRepositoryInteractor
import org.koin.dsl.module

val trackModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(
            get(),
            get(),
            get()
        )
    }

    single<PlayListRepository>
    {
        PlayListRepositoryImpl(
            get(),
            get()
        )
    }

    single <PlayListInteractor>{
        PlayListInteractorImpl(get())
    }

    single {
        TrackRepositoryInteractor(get())
    }

    single <LikedHistoryRepository>{
        LikedHistoryRepositoryImpl(get(),get())
    }

    single <LikedHistoryInteractor>{
        LikedHistoryInteractorImpl(get())
    }
}