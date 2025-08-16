package com.practicum.playlist_maker_one.domain.api

import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepositoryInteractor {
    fun searchTracks(query: String) : Flow<Pair<List<TrackData>?, String?>>
}