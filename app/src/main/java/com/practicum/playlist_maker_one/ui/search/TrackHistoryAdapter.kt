package com.practicum.playlist_maker_one.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker_one.Creator
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.data.dto.TrackHistoryManagerImpl
import com.practicum.playlist_maker_one.domain.entity.TrackData

class TrackHistoryAdapter (private val context: Context, private var trackHistory: List<TrackData>
) : RecyclerView.Adapter<TrackViewHolder> () {

    val history = Creator.getTrackManager()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackHistory[position])

        holder.itemView.setOnClickListener{
            history.putLastTrack(Creator.getMapper().reversedMap(trackHistory[position]))
            val displayIntent = Intent(context, AudioActivity::class.java)
            context.startActivity(displayIntent)
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