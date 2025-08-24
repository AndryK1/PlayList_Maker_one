package com.practicum.playlist_maker_one.domain.impl

import com.practicum.playlist_maker_one.domain.db.LikedHistoryInteractor
import com.practicum.playlist_maker_one.domain.db.LikedHistoryRepository
import com.practicum.playlist_maker_one.domain.entity.TrackData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LikedHistoryInteractorImpl(private val repository : LikedHistoryRepository ) : LikedHistoryInteractor {

    override suspend fun invoke(track: TrackData, isFavorite: Boolean) : Boolean {
        return when {
            isFavorite -> {
                repository.deleteTrack(track)
                false
            }
            else -> {
                repository.saveTrack(track)
                true
            }
        }
    }

    override suspend fun isTrackFavorite(trackData: TrackData): Boolean {
        return repository.isTrackInFavorite(trackData.trackId)
    }

    override fun getLikedTracks(): Flow<List<TrackData>> {
        return repository.getLikedTracks()
    }
}