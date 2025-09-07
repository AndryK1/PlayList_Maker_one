package com.practicum.playlist_maker_one.domain.impl

import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.db.PlayListRepository
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val repository: PlayListRepository
)  : PlayListInteractor{

    override suspend fun savePlayList(playList: PlayListData) {
        repository.savePlayList(playList)
    }

    override fun getPlayLists(): Flow<List<PlayListData>> {
        return repository.getPlayLists()
    }

    override suspend fun addTrackToPlayList(
        trackId: Long,
        playList: PlayListData
    ) {
        repository.addTrackToPlayList(trackId,playList)
    }

    override suspend fun deleteTrackFromPlayList(
        trackId: Long,
        playList: PlayListData
    ) {
        repository.deleteTrackFromPlayList(trackId,playList)
    }


}