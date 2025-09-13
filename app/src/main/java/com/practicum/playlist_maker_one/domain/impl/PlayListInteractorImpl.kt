package com.practicum.playlist_maker_one.domain.impl

import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.db.PlayListRepository
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
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
        trackId: TrackData,
        playList: PlayListData
    ) {
        repository.addTrackToPlayList(trackId,playList)
    }

    override suspend fun deleteTrackFromPlayList(
        track: TrackData,
        playList: PlayListData
    ) : PlayListData{
        return repository.deleteTrackFromPlayList(track,playList)
    }

    override suspend fun updatePlaylist(playList: PlayListData) {
        repository.updatePlaylist(playList)
    }

    override suspend fun deletePlaylist(playList: PlayListData) {
        repository.deletePlaylist(playList)
    }


}