package com.practicum.playlist_maker_one.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker_one.data.db.PlayListEntity
import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData

class PlayListDbConverter {

    fun map(playListData: PlayListData) : PlayListEntity{
        return PlayListEntity(
            id = playListData.id,
            name = playListData.name,
            description = playListData.description,
            tracks = listToJson(playListData.tracks),
            uri = playListData.imageUrl,
            tracksCount = playListData.tracksCount
        )
    }

    fun map(playListData: PlayListEntity) : PlayListData{
        return PlayListData(
            id = playListData.id,
            name = playListData.name,
            description = playListData.description,
            tracks = jsonToList(playListData.tracks),
            imageUrl = playListData.uri,
            tracksCount = playListData.tracksCount
        )
    }

    private fun listToJson(tracks: List<TrackData>) : String{
        val gson = Gson()
        return gson.toJson(tracks)
    }

    private fun jsonToList(tracks: String) : List<TrackData>{
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<TrackData>>() {}.type
            gson.fromJson(tracks,type) ?: emptyList()
        } catch( e : Exception){
            emptyList()
        }
    }
}