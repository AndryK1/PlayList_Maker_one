package com.practicum.playlist_maker_one.data.db

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

@Entity(tableName = "playlist_table")
data class PlayListEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name : String,
    val description : String,
    val uri: String,
    val tracksIds : String,
    val tracksCount : Long
    )