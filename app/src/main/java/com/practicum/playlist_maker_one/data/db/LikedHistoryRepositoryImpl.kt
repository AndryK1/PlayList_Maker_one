package com.practicum.playlist_maker_one.data.db

import com.practicum.playlist_maker_one.data.converters.TrackDbConverter
import com.practicum.playlist_maker_one.domain.db.LikedHistoryRepository
import com.practicum.playlist_maker_one.domain.entity.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LikedHistoryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : LikedHistoryRepository {

    override fun getLikedTracks(): Flow<List<TrackData>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(fromEntityToTrackData(tracks))
    }

    override suspend fun saveTrack(track: TrackData) {
        appDatabase.trackDao().insertTrack(fromTrackDataToEntity(track))
    }

    override suspend fun deleteTrack(track: TrackData) {
        appDatabase.trackDao().deleteTrackEntity(fromTrackDataToEntity(track))
    }

    override suspend fun isTrackInFavorite(trackId: Long): Boolean {
        return appDatabase.trackDao().isTrackInFavorites(trackId) != null
    }

    private fun fromEntityToTrackData(tracks : List<TrackEntity>) : List<TrackData>{
        return tracks.map { track -> trackDbConverter.map(track) }
    }

    private fun fromTrackDataToEntity(track : TrackData) : TrackEntity{
        return trackDbConverter.map(track)
    }
}