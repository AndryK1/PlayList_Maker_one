package com.practicum.playlist_maker_one.ui.media

import com.practicum.playlist_maker_one.ui.search.TrackViewHolder
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


class TrackFavoritesAdapter (
    private val onItemClick: (TrackData) -> Unit,
    private var track: List<TrackData>
) : RecyclerView.Adapter<TrackViewHolder> () {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])

        holder.itemView.setOnClickListener{

            onItemClick(track[position])
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