package com.practicum.playlist_maker_one.domain.api

import android.content.Context
import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.entity.TrackData

interface TrackHistoryManager {

    fun initializeHistory()
    fun addTrackToHistory(track: TrackDataDto)
    fun deliteHistory()
    fun getTrackHistory(): List<TrackDataDto>
    fun putLastTrack(track: TrackDataDto)
    fun getLastTrack(): TrackDataDto


}