package com.practicum.playlist_maker_one.domain.useCase

import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.api.TrackRepositoryInteractor
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TrackRepositoryInteractor(private val repository: TrackRepository) : TrackRepositoryInteractor {
    override fun searchTracks(query: String): Flow<Pair<List<TrackData>?, String?>> {

        return repository.searchTracks(query).map { result ->

            when(result){
                is Resource.Error ->{
                    Pair(null, result.message)
                }
                is Resource.Success ->{
                    Pair(result.data, null)
                }
            }
        }
    }
}

