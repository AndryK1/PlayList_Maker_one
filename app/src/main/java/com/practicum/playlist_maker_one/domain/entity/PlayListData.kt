package com.practicum.playlist_maker_one.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayListData (
    val id : Long,
    val name : String,
    val description : String,
    val imageUrl: String,
    val tracks : List<TrackData>,
    val tracksCount : Int
) : Parcelable