package com.practicum.playlist_maker_one

import com.practicum.playlist_maker_one.data.dto.SharedPrefsImpl
import com.practicum.playlist_maker_one.data.dto.TrackHistoryManagerImpl
import com.practicum.playlist_maker_one.data.dto.TrackRepositoryImpl
import com.practicum.playlist_maker_one.data.network.RetrofitNetworkClient
import com.practicum.playlist_maker_one.domain.api.NetworkClient
import com.practicum.playlist_maker_one.domain.api.SharedPrefs
import com.practicum.playlist_maker_one.domain.api.ThemeManager
import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.api.trackPlayer
import com.practicum.playlist_maker_one.domain.impl.TrackMapperImpl
import com.practicum.playlist_maker_one.domain.impl.TrackPlayerImpl
import com.practicum.playlist_maker_one.domain.useCase.TrackRepositoryInteractor

object Creator {
    fun getSharedPrefs() : SharedPrefs{
        return SharedPrefsImpl()
    }

    fun getTrackManager() : TrackHistoryManager{
        return TrackHistoryManagerImpl
    }

    fun getMapper() : TrackMapper{
        return TrackMapperImpl()
    }

    fun provideNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }

    fun provideTrackRepository(networkClient: NetworkClient): TrackRepository {
        return TrackRepositoryImpl(networkClient)
    }

    fun provideTrackUseCase(repository: TrackRepository): TrackRepositoryInteractor {
        return TrackRepositoryInteractor(repository)
    }

    fun getMediaPlayer() : trackPlayer{
        return TrackPlayerImpl()
    }

    fun getThemeManager() : ThemeManager{
        return SharedPrefsImpl()
    }
}