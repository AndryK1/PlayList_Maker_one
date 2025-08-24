package com.practicum.playlist_maker_one.domain.db

import com.practicum.playlist_maker_one.domain.entity.TrackData
import kotlinx.coroutines.flow.Flow

interface LikedHistoryInteractor {

    suspend fun invoke(track: TrackData, isFavorite : Boolean) : Boolean
    suspend fun isTrackFavorite(trackData: TrackData) : Boolean
    fun getLikedTracks() : Flow<List<TrackData>>
}