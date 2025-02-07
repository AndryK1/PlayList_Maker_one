package com.practicum.playlist_maker_one

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.view.menu.MenuView.ItemView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object TrackHistoryManager {

    private var trackHistory = mutableListOf<TrackData>()

    fun initializeHistory(context: Context) {
        trackHistory = getHistory(context)
    }

    fun addTrackToHistory(track: TrackData) {

        if ((trackHistory.size < 10) && !trackHistory.contains(track)) {
            trackHistory.add(0,track)
        }
        else if ((trackHistory.size >= 10) && !trackHistory.contains(track)) {
            trackHistory.removeAt(0)
            trackHistory.add(0,track)
        }
        else if (trackHistory.contains(track)) {
            trackHistory.remove(track)
            trackHistory.add(0, track)
        }
    }

    fun saveHistory( context : Context){
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = Gson().toJson(trackHistory)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()

    }

    private fun getHistory(context : Context) : MutableList<TrackData>{
        val sharedPrefs = context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val json = sharedPrefs.getString(HISTORY_KEY, null)
        var list: MutableList<TrackData> = mutableListOf()
        if(json != null)
        {
            val type = object : TypeToken<MutableList<TrackData>>() {}.type
            list = Gson().fromJson(json, type)
            return list
        }
        else{
            return list
        }
    }

    fun deliteHistory(context: Context){
        trackHistory.clear()
        saveHistory(context)
    }

    fun getTrackHistory(): List<TrackData> {
        return trackHistory
    }
}