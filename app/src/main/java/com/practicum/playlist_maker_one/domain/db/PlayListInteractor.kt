package com.practicum.playlist_maker_one.domain.db

import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun savePlayList(playList: PlayListData)

    fun getPlayLists() : Flow<List<PlayListData>>

    suspend fun addTrackToPlayList(trackId: TrackData, playList: PlayListData)

    suspend fun deleteTrackFromPlayList(track: TrackData, playList: PlayListData) : PlayListData

    suspend fun updatePlaylist(playList: PlayListData)

    suspend fun deletePlaylist(playList: PlayListData)
}