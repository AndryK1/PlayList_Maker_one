package com.practicum.playlist_maker_one

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class TrackAdapter (private val track: List<TrackData>
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])

    }


    override fun getItemCount(): Int {
        return track.size
    }

}

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val tvTrackName: TextView = itemView.findViewById(R.id.songName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.ArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val icSong: ImageView = itemView.findViewById(R.id.songIcon)

    fun bind (item : TrackData)
    {
        val artworkUrl : String = item.artworkUrl100

        Glide.with(itemView)
            .load(artworkUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerInside()
            .transform(RoundedCorners(2))
            .into(icSong)

        tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        tvTrackName.text = item.trackName.trim()
        tvArtistName.text = item.artistName.trim()
    }


}