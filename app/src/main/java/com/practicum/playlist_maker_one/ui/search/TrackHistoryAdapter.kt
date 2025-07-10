package com.practicum.playlist_maker_one.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack
import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.player.activity.AudioFragment
import org.koin.java.KoinJavaComponent.inject

class TrackHistoryAdapter (private val onItemClick: (TrackData) -> Unit,
                           private var trackHistory: List<TrackData>,
                           private val history : TrackHistoryManager,
                           private val mapper : TrackMapper,
                           private val sharedPrefs: SharedPrefsTrack
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackHistory[position])

        holder.itemView.setOnClickListener{

            history.addTrackToHistory(mapper.reversedMap(trackHistory[position]))
            sharedPrefs.saveHistory(history.getTrackHistory())

            onItemClick(trackHistory[position])
        }

    }

    fun updateData(newData: List<TrackData>) {
        trackHistory = newData
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return trackHistory.size
    }
}