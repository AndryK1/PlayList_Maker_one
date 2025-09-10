package com.practicum.playlist_maker_one.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker_one.data.db.PlayListEntity
import com.practicum.playlist_maker_one.domain.entity.PlayListData

class PlayListDbConverter {

    fun map(playListData: PlayListData) : PlayListEntity{
        return PlayListEntity(
            id = playListData.id,
            name = playListData.name,
            description = playListData.description,
            tracksIds = listToJson(playListData.tracksIds),
            uri = playListData.imageUrl,
            tracksCount = playListData.tracksCount
        )
    }

    fun map(playListData: PlayListEntity) : PlayListData{
        return PlayListData(
            id = playListData.id,
            name = playListData.name,
            description = playListData.description,
            tracksIds = jsonToList(playListData.tracksIds),
            imageUrl = playListData.uri,
            tracksCount = playListData.tracksCount
        )
    }

    private fun listToJson(playList: List<Long>) : String{
        val gson = Gson()
        return gson.toJson(playList)
    }

    private fun jsonToList(playList: String) : List<Long>{
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<Long>>() {}.type
            gson.fromJson(playList,type) ?: emptyList()
        } catch( e : Exception){
            emptyList()
        }
    }
}