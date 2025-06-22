package com.practicum.playlist_maker_one.data.search.dto

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack


class SharedPrefsTracksImpl : SharedPrefsTrack{
    companion object{
        const val APP_PREFERENCES = "AppPreferences"
        const val HISTORY_KEY = "search_history_key"
    }

    override fun saveHistory(history: List<TrackDataDto>, context : Context){
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = Gson().toJson(history)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    override fun getHistory(context : Context) : MutableList<TrackDataDto>{
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = sharedPrefs.getString(HISTORY_KEY, null)
        var list: MutableList<TrackDataDto> = mutableListOf()
        if(json != null)
        {
            val type = object : TypeToken<MutableList<TrackDataDto>>() {}.type
            list = Gson().fromJson(json, type)
            return list
        }
        else{
            return list
        }
    }
}