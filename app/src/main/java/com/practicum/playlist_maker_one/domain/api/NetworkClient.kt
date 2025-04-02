package com.practicum.playlist_maker_one.domain.api

import com.practicum.playlist_maker_one.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any) : Response
}