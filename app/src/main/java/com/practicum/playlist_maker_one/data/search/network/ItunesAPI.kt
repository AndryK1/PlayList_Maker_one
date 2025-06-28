package com.practicum.playlist_maker_one.data.search.network

import com.practicum.playlist_maker_one.data.search.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<TrackSearchResponse>
}