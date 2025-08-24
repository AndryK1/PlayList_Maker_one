package com.practicum.playlist_maker_one.domain.impl

import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.entity.TrackData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrackMapperImpl : TrackMapper{

    fun parseMinutesAndSecondsToMillis(time: String): Int {
        val parts = time.split(":")
        val minutes = parts[0].toInt()
        val seconds = parts[1].toInt()
        return (minutes * 60 + seconds) * 1000
    }


    override fun map(trackDataDto: TrackDataDto, isFavorite : Boolean): TrackData {
        return TrackData(
            trackId = trackDataDto.trackId,
            trackName = trackDataDto.trackName,
            artistName = trackDataDto.artistName,
            trackFormatedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDataDto.trackTimeMillis),
            formatedArtworkUrl100 = trackDataDto.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"),
            previewUrl = trackDataDto.previewUrl,
            collectionName = trackDataDto.collectionName,
            releaseDateFormated = SimpleDateFormat("yyyy", Locale.getDefault()).format(trackDataDto.releaseDate),
            primaryGenreName = trackDataDto.primaryGenreName,
            country = trackDataDto.country,
            isFavorite = isFavorite
        )
    }

    override fun reversedMap(trackData: TrackData) : TrackDataDto{
        val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
        return TrackDataDto(
            trackId = trackData.trackId,
            trackName = trackData.trackName,
            artistName = trackData.artistName,
            trackTimeMillis = parseMinutesAndSecondsToMillis(trackData.trackFormatedTime),
            artworkUrl100 = trackData.formatedArtworkUrl100.replaceAfterLast('/',"100x100bb.jpg"),
            previewUrl = trackData.previewUrl,
            collectionName = trackData.collectionName,
            releaseDate = yearFormatter.parse(trackData.releaseDateFormated)!!,
            primaryGenreName = trackData.primaryGenreName,
            country = trackData.country
        )
    }


}