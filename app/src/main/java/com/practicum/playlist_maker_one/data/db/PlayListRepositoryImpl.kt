package com.practicum.playlist_maker_one.data.db

import com.practicum.playlist_maker_one.data.converters.PlayListDbConverter
import com.practicum.playlist_maker_one.domain.db.PlayListRepository
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlayListDbConverter
) : PlayListRepository {

    override fun getPlayLists(): Flow<List<PlayListData>> = flow{
        val playLists = appDatabase.playListDao().getPlayLists()
        emit(fromEntityToData(playLists))
    }

    override suspend fun savePlayList(playList: PlayListData) {
        appDatabase.playListDao().insertPlayList(fromDataToEntity(playList))
    }

    override suspend fun updatePlaylist(playList: PlayListData){
        appDatabase.playListDao().updatePlayList(fromDataToEntity(playList))
    }

    override suspend fun addTrackToPlayList(track: TrackData, playList: PlayListData) {
        val currentTrackIds = playList.tracks.map { it.trackId }

        if (!currentTrackIds.contains(track.trackId)) {
            val updatedTracks = playList.tracks.toMutableList().apply {
                add(track)
            }
            val updatedEntity = playList.copy(
                tracks = updatedTracks,
                tracksCount = updatedTracks.size
            )
            appDatabase.playListDao().updatePlayList(converter.map(updatedEntity))
        }
    }

    override suspend fun deletePlaylist(playList: PlayListData){
        appDatabase.playListDao().deleteTrackEntity(fromDataToEntity(playList))
    }

    override suspend fun deleteTrackFromPlayList(track: TrackData, playList: PlayListData) : PlayListData{
        val currentTrackIds = playList.tracks.map { it.trackId }

        return if(currentTrackIds.contains(track.trackId)){
            val updatedTracks = playList.tracks.filterNot { it.trackId == track.trackId }
            val updatedPlaylist = playList.copy(
                tracks = updatedTracks,
                tracksCount = updatedTracks.size
            )
            appDatabase.playListDao().updatePlayList(converter.map(updatedPlaylist))
            updatedPlaylist
        }else{
            playList
        }
    }

    private fun fromEntityToData(list: List<PlayListEntity>) : List<PlayListData>{
        return list.map { entity -> converter.map(entity) }
    }

    private fun fromEntityToData(entity: PlayListEntity) : PlayListData{
        return converter.map(entity)
    }

    private fun fromDataToEntity(item: PlayListData) : PlayListEntity{
        return  converter.map(item)
    }
}