package com.practicum.playlist_maker_one.data.search.dto

import android.content.Context
import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.util.Creator

import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager



object TrackHistoryManagerImpl : TrackHistoryManager{

    private var trackHistory = mutableListOf<TrackDataDto>()
    private lateinit var lastTrack : TrackDataDto
    private var sharedPrefs = Creator.getSharedPrefs()

    override fun initializeHistory() {
        trackHistory = sharedPrefs.getHistory()
    }

    override fun addTrackToHistory(track: TrackDataDto) {

        if ((trackHistory.size < 10) && !trackHistory.contains(track)) {
            trackHistory.add(0,track)
        }
        else if ((trackHistory.size >= 10) && !trackHistory.contains(track)) {
            trackHistory.removeAt(9)
            trackHistory.add(0,track)
        }
        else if (trackHistory.contains(track)) {
            trackHistory.remove(track)
            trackHistory.add(0, track)
        }
    }

    override fun deliteHistory(){
        trackHistory.clear()
    }

    override fun getTrackHistory(): List<TrackDataDto> {
        return trackHistory
    }

    override fun putLastTrack(track: TrackDataDto){
        lastTrack = track
    }

    override fun getLastTrack(): TrackDataDto {
        return lastTrack
    }

}