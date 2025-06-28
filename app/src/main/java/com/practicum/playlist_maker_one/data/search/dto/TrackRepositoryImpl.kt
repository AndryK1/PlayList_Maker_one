package com.practicum.playlist_maker_one.data.search.dto

import com.practicum.playlist_maker_one.util.Creator
import com.practicum.playlist_maker_one.domain.api.NetworkClient
import com.practicum.playlist_maker_one.domain.api.TrackRepository
import com.practicum.playlist_maker_one.domain.entity.TrackData

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    private var searchThread: Thread? = null

    //флаг volatile - отвечает за видимость изменений переменной между потоками
    @Volatile private var isSearchCancelled = false

    override fun searchTracks(query: String, callback: (Result<List<TrackData>>) -> Unit) {
        isSearchCancelled = false
        searchThread?.interrupt()//прерываем предыдущие потоки

        searchThread = Thread {
            val mapper = Creator.getMapper()
            val request = TrackSearchRequest(query)

            val response = networkClient.doRequest(request)

            if (response.resultCode == 200) {
                if (response is TrackSearchResponse) {
                    val result = response.results.map { mapper.map(it) }
                    callback(Result.success(result))
                }
            }
            else if(response.resultCode == 100){
                callback(Result.success(emptyList()))
            }
            else {
                callback(Result.failure(Exception("Ошибка при загрузке данных")))
            }
        }.apply(){start()}
    }

    override fun canselThread(){
        isSearchCancelled = true
        searchThread?.interrupt()
        searchThread = null

    }
}

