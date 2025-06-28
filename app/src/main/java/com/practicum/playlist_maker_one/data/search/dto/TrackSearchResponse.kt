package com.practicum.playlist_maker_one.data.search.dto

import com.practicum.playlist_maker_one.data.dto.TrackDataDto

class TrackSearchResponse(val searchType: String,
                          val expression: String,
                          val results: List<TrackDataDto>) : Response()
