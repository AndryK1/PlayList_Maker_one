package com.practicum.playlist_maker_one.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.TrackData


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val tvTrackName: TextView = itemView.findViewById(R.id.songName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.ArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val icSong: ImageView = itemView.findViewById(R.id.songIcon)

    fun bind (item : TrackData)
    {
        val artworkUrl : String = item.formatedArtworkUrl100

        Glide.with(itemView)
            .load(artworkUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerInside()
            .transform(RoundedCorners(2))
            .into(icSong)

        tvTrackTime.text = item.trackFormatedTime

        tvTrackName.text = item.trackName.trim()
        tvArtistName.text = item.artistName.trim()
    }


}