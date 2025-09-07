package com.practicum.playlist_maker_one.domain.entity

data class PlayListData (
    val id : Long,
    val name : String,
    val description : String,
    val imageUrl: String,
    val tracksIds : List<Long>,
    val tracksCount : Long
)