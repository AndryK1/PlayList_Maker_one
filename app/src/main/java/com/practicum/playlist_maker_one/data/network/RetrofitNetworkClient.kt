package com.practicum.playlist_maker_one.data.network

import com.practicum.playlist_maker_one.domain.api.NetworkClient
import com.practicum.playlist_maker_one.data.dto.Response
import com.practicum.playlist_maker_one.data.dto.TrackSearchRequest
import com.practicum.playlist_maker_one.data.dto.TrackSearchResponse
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient() : NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesAPI::class.java)

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