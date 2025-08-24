package com.practicum.playlist_maker_one.data.search.dto

import com.practicum.playlist_maker_one.data.db.AppDatabase
import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.api.NetworkClient
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper : TrackMapper,
    private val appDatabase: AppDatabase
) : TrackRepository {

    //флаг volatile - отвечает за видимость изменений переменной между потоками
//    @Volatile private var isSearchCancelled = false

    override fun searchTracks(query: String) : Flow<Resource<List<TrackData>>> = flow {
//        isSearchCancelled = false

            val response = networkClient.doRequest(TrackSearchRequest(query))

            when(response.resultCode) {
                200 -> {
                    if (response is TrackSearchResponse) {

                        val trackIds = response.results.map { it.trackId }

                        val favorites = appDatabase.trackDao().getFavoriteTrackIds(trackIds)

                        val data = response.results.map {
                            mapper.map(it, favorites.contains(it.trackId))
                        }
                        emit(Resource.Success(
                            data
                        ))
                    }
                }
                100 -> {
                    emit(Resource.Error("No tracks error", emptyList()))
                }
                400 -> {
                    emit(Resource.Error("Ошибка при загрузке данных", emptyList()))
                }
            }
        }


}

//    override fun canselThread(){
//        isSearchCancelled = true
//        searchThread?.interrupt()
//        searchThread = null
//
//    }

