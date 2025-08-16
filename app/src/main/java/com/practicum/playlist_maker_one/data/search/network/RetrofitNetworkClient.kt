package com.practicum.playlist_maker_one.data.search.network

import com.practicum.playlist_maker_one.domain.api.NetworkClient
import com.practicum.playlist_maker_one.data.search.dto.Response
import com.practicum.playlist_maker_one.data.search.dto.TrackSearchRequest
import com.practicum.playlist_maker_one.data.search.dto.TrackSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(
    private val itunesService : ItunesAPI
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {

            return withContext(Dispatchers.IO) {

                try {
                    val response = itunesService.search(dto.request)

                    if (response.results.isEmpty()) {
                        Response().apply { resultCode = 100 }
                        } else {
                            TrackSearchResponse(
                                searchType = "search",
                                expression = dto.request,
                                results = response.results
                            ).apply { resultCode = 200 }
                        }

                } catch (e: Exception) {
                    Response().apply { resultCode = 400 }
                }
            }
        }
        return Response().apply { resultCode = 400 }
    }
}