package com.practicum.playlist_maker_one.domain.db

import com.practicum.playlist_maker_one.domain.entity.PlayListData
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun savePlayList(playList: PlayListData)

    fun getPlayLists() : Flow<List<PlayListData>>

    suspend fun addTrackToPlayList(trackId: Long, playList: PlayListData)

    suspend fun deleteTrackFromPlayList(trackId: Long, playList: PlayListData)
}