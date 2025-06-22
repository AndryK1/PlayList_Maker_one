package com.practicum.playlist_maker_one.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker_one.util.Creator
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.player.activity.AudioActivity


class TrackAdapter (private val context: Context, private var track: List<TrackData>
) : RecyclerView.Adapter<TrackViewHolder> () {

    private val history = Creator.getTrackManager()
    private val mapper = Creator.getMapper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])

        holder.itemView.setOnClickListener{
            history.addTrackToHistory(mapper.reversedMap(track[position]))
            Creator.getSharedPrefs().saveHistory(history.getTrackHistory() ,holder.itemView.context)
            history.putLastTrack(mapper.reversedMap(track[position]))
            val displayIntent = Intent(context, AudioActivity::class.java)
            context.startActivity(displayIntent)
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