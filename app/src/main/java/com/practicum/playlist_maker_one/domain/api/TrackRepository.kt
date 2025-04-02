package com.practicum.playlist_maker_one.domain.api

import com.practicum.playlist_maker_one.domain.entity.TrackData

interface TrackRepository {
    fun searchTracks(query: String, callback: (Result<List<TrackData>>) -> Unit)
    fun canselThread()
}