package com.practicum.playlist_maker_one.domain.api

import android.content.Context
import com.practicum.playlist_maker_one.data.dto.TrackDataDto

interface SharedPrefs {
    fun saveHistory(history: List<TrackDataDto>, context : Context)
    fun getHistory(context : Context) : MutableList<TrackDataDto>
}