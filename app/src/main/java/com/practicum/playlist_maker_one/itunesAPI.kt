package com.practicum.playlist_maker_one

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//
interface itunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<TrackResponse>
}