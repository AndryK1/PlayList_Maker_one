package com.practicum.playlist_maker_one.util

import android.content.Context
import com.practicum.playlist_maker_one.data.settings.SharedPrefsThemeImpl
import com.practicum.playlist_maker_one.data.search.dto.SharedPrefsTracksImpl
import com.practicum.playlist_maker_one.data.search.dto.TrackHistoryManagerImpl
import com.practicum.playlist_maker_one.data.search.dto.TrackRepositoryImpl
import com.practicum.playlist_maker_one.data.search.network.RetrofitNetworkClient
import com.practicum.playlist_maker_one.domain.api.NetworkClient
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack
import com.practicum.playlist_maker_one.domain.api.ThemeManager
import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.api.TrackPlayer
import com.practicum.playlist_maker_one.domain.impl.TrackMapperImpl
import com.practicum.playlist_maker_one.data.player.dto.TrackPlayerImpl
import com.practicum.playlist_maker_one.domain.useCase.TrackRepositoryInteractor

object Creator {

    private lateinit var appContext : Context

    fun init(context: Context){
        appContext = context.applicationContext
    }

    fun getSharedPrefs() : SharedPrefsTrack{
        return SharedPrefsTracksImpl(appContext)
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

    fun getMediaPlayer() : TrackPlayer{
        return TrackPlayerImpl()
    }

    fun getThemeManager() : ThemeManager{
        return SharedPrefsThemeImpl(appContext)
    }
}