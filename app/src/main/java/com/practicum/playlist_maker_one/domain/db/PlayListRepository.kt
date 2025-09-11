package com.practicum.playlist_maker_one.domain.db

import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    fun getPlayLists() : Flow<List<PlayListData>>

    suspend fun savePlayList(playList : PlayListData)

    suspend fun updatePlaylist(playList: PlayListData)

    suspend fun addTrackToPlayList(track: TrackData, playList: PlayListData)

    suspend fun deleteTrackFromPlayList(track: TrackData, playList: PlayListData) : PlayListData

    suspend fun deletePlaylist(playList: PlayListData)
}