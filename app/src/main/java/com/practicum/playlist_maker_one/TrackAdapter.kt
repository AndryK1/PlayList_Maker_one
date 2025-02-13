package com.practicum.playlist_maker_one

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale


class TrackAdapter (private val context: Context, private val track: List<TrackData>
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])

        holder.itemView.setOnClickListener{
            TrackHistoryManager.addTrackToHistory(track[position])
            TrackHistoryManager.saveHistory(holder.itemView.context)
            TrackHistoryManager.putLastTrack(track[position])
            val displayIntent = Intent(context, AudioActivity::class.java)
            context.startActivity(displayIntent)
        }
    }


    override fun getItemCount(): Int {
        return track.size
    }



}