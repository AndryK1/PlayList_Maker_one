package com.practicum.playlist_maker_one.data.search.dto

import android.content.Context
import com.practicum.playlist_maker_one.data.db.AppDatabase
import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack

import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import com.practicum.playlist_maker_one.domain.entity.TrackData
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.java.KoinJavaComponent.inject


class TrackHistoryManagerImpl(
    private val appDatabase: AppDatabase
) : TrackHistoryManager{

    private var trackHistory = mutableListOf<TrackDataDto>()
    private lateinit var lastTrack : TrackDataDto

    private val sharedPrefs: SharedPrefsTrack by lazy {
        getKoin().get()
    }

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

    override suspend fun getFavorites(track : TrackDataDto) : Boolean{
        val trackIds = trackHistory.map { it.trackId }

        val favoriteIds = appDatabase.trackDao().getFavoriteTrackIds(trackIds)

        return favoriteIds.contains(track.trackId)

    }

    override fun deleteHistory(){
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