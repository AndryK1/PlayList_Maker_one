package com.practicum.playlist_maker_one.data.converters

import com.practicum.playlist_maker_one.data.db.TrackEntity
import com.practicum.playlist_maker_one.domain.entity.TrackData
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDbConverter {

    fun parseMinutesAndSecondsToMillis(time: String): Int {
        val parts = time.split(":")
        val minutes = parts[0].toInt()
        val seconds = parts[1].toInt()
        return (minutes * 60 + seconds) * 1000
    }

    fun map(track: TrackEntity) : TrackData {
        return TrackData(
            trackId = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackFormatedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
            formatedArtworkUrl100 = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"),
            previewUrl = track.previewUrl,
            collectionName = track.collectionName,
            releaseDateFormated = SimpleDateFormat("yyyy", Locale.getDefault()).format(track.releaseDate),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            isFavorite = true
        )
    }

    fun map(track: TrackData) : TrackEntity{

        return TrackEntity(
            id =track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = parseMinutesAndSecondsToMillis(track.trackFormatedTime),
            artworkUrl100 = track.formatedArtworkUrl100.replaceAfterLast('/',"100x100bb.jpg"),
            previewUrl = track.previewUrl,
            collectionName = track.collectionName,
            releaseDate = track.releaseDateFormated.toLong(),
            primaryGenreName = track.primaryGenreName,
            country = track.country
        )
    }
}