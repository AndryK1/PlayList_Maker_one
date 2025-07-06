package com.practicum.playlist_maker_one.data.search.dto

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack


class SharedPrefsTracksImpl(
    private val context: Context,
    private val gson: Gson
    ) : SharedPrefsTrack{

    override fun saveHistory(history: List<TrackDataDto>){
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)//сделать позже -  SharedPreferences можно сразу создавать с помощью DI и передаывать сюда вместо Context
        val json = gson.toJson(history)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    override fun getHistory() : MutableList<TrackDataDto>{
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = sharedPrefs.getString(HISTORY_KEY, null)
        var list: MutableList<TrackDataDto> = mutableListOf()
        if(json != null)
        {
            val type = object : TypeToken<MutableList<TrackDataDto>>() {}.type
            list = gson.fromJson(json, type)
            return list
        }
        else{
            return list
        }
    }

    companion object{
        const val APP_PREFERENCES = "AppPreferences"
        const val HISTORY_KEY = "search_history_key"
    }
}