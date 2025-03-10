package com.practicum.playlist_maker_one

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackHistoryAdapter (private val context: Context, private val trackHistory: List<TrackData>
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackHistory[position])
        holder.itemView.setOnClickListener{
            TrackHistoryManager.putLastTrack(trackHistory[position])
            val displayIntent = Intent(context, AudioActivity::class.java)
            context.startActivity(displayIntent)
        }

    }


    override fun getItemCount(): Int {
        return trackHistory.size
    }
}