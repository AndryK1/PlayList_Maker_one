package com.practicum.playlist_maker_one.domain.useCase

import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.entity.TrackData

class TrackRepositoryInteractor(private val repository: TrackRepository) {
    fun execute(query: String, callback: (Result<List<TrackData>>) -> Unit) {
        repository.searchTracks(query, callback)
    }

    fun destroy(){
        repository.canselThread()
    }
}