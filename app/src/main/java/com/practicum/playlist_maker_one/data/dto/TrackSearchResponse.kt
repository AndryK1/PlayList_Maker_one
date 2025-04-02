package com.practicum.playlist_maker_one.data.dto

class TrackSearchResponse(val searchType: String,
                          val expression: String,
                          val results: List<TrackDataDto>) : Response()
