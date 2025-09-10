package com.practicum.playlist_maker_one.domain.db

import com.practicum.playlist_maker_one.domain.entity.PlayListData
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    fun getPlayLists() : Flow<List<PlayListData>>

    suspend fun savePlayList(playList : PlayListData)

    suspend fun addTrackToPlayList(trackId: Long, playList: PlayListData)

    suspend fun deleteTrackFromPlayList(trackId: Long, playList: PlayListData)
}