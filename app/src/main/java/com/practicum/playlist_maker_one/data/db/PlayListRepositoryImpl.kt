package com.practicum.playlist_maker_one.data.db

import com.practicum.playlist_maker_one.data.converters.PlayListDbConverter
import com.practicum.playlist_maker_one.domain.db.PlayListRepository
import com.practicum.playlist_maker_one.domain.entity.PlayListData
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

    override suspend fun addTrackToPlayList(trackId: Long, playList: PlayListData){
        val currentTrackIds = playList.tracksIds

        if(!currentTrackIds.contains(trackId)){
            val updatedTrackIds = currentTrackIds.toMutableList().apply { add(trackId) }
            val updatedEntity = playList.copy(
                tracksIds = updatedTrackIds,
                tracksCount = updatedTrackIds.size.toLong()
            )
            appDatabase.playListDao().updatePlayList(converter.map(updatedEntity))
        }
    }

    override suspend fun deleteTrackFromPlayList(trackId: Long, playList: PlayListData){
        val currentTrackIds = playList.tracksIds

        if(currentTrackIds.contains(trackId)){
            val updatedTrackIds = currentTrackIds.toMutableList().apply { remove(trackId) }
            val updatedEntity = playList.copy(
                tracksIds = updatedTrackIds,
                tracksCount = updatedTrackIds.size.toLong()
            )
            appDatabase.playListDao().updatePlayList(converter.map(updatedEntity))
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