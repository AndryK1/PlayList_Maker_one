package com.practicum.playlist_maker_one.domain.db

import com.practicum.playlist_maker_one.data.db.dao.TrackDao
import com.practicum.playlist_maker_one.domain.entity.TrackData
import kotlinx.coroutines.flow.Flow

interface LikedHistoryRepository {

    fun getLikedTracks() : Flow<List<TrackData>>

    suspend fun saveTrack(track : TrackData)

    suspend fun deleteTrack(track : TrackData)

    suspend fun isTrackInFavorite(trackId: Long) : Boolean
}