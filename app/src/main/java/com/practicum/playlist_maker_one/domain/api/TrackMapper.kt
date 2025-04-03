package com.practicum.playlist_maker_one.domain.api

import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.entity.TrackData

interface TrackMapper{
    fun map(trackDataDto: TrackDataDto) : TrackData
    fun reversedMap(trackData: TrackData) : TrackDataDto
}