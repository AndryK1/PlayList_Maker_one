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


class TrackAdapter (
    private val onItemClick: (TrackData) -> Unit,
    private var track: List<TrackData>,
    private val history: TrackHistoryManager,
    private val mapper: TrackMapper,
    private val sharedPrefs: SharedPrefsTrack,
    private val onLongClickListener: ((TrackData) -> Unit)? = null
) : RecyclerView.Adapter<TrackViewHolder> () {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])

        holder.itemView.setOnClickListener{

            history.addTrackToHistory(mapper.reversedMap(track[position]))
            sharedPrefs.saveHistory(history.getTrackHistory())

            onItemClick(track[position])

        }

        onLongClickListener?.let { longClickListener ->
            holder.itemView.setOnLongClickListener {
                longClickListener(track[position])
                true
            }
        }
    }

    fun updateData(newData: List<TrackData>) {
        track = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return track.size
    }



}