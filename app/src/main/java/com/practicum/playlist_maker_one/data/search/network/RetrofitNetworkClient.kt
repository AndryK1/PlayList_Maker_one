package com.practicum.playlist_maker_one.data.search.network

import com.practicum.playlist_maker_one.domain.api.NetworkClient
import com.practicum.playlist_maker_one.data.search.dto.Response
import com.practicum.playlist_maker_one.data.search.dto.TrackSearchRequest
import com.practicum.playlist_maker_one.data.search.dto.TrackSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(
    private val itunesService : ItunesAPI
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val call = itunesService.search(dto.request)

            return try {
                val response = call.execute()
                if (response.isSuccessful) {
                    val trackListDto = response.body()?.results ?: emptyList()
                    if (trackListDto.isEmpty()) {
                        Response().apply { resultCode = 100 }
                    } else {
                        TrackSearchResponse(
                            searchType = "search",
                            expression = dto.request,
                            results = trackListDto
                        ).apply { resultCode = 200 }
                    }
                } else {
                    Response().apply { resultCode = 400 }
                }
            } catch (e: Exception) {
                Response().apply { resultCode = 400 }
            }
        }
        return Response().apply { resultCode = 400 }
    }
}