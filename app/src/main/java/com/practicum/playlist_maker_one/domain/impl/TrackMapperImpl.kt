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


    override fun map(trackDataDto: TrackDataDto, isFavorite: Boolean): TrackData {
        return TrackData(
            trackId = trackDataDto.trackId,
            trackName = trackDataDto.trackName,
            artistName = trackDataDto.artistName ?: "nothing found",
            trackFormatedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDataDto.trackTimeMillis),
            formatedArtworkUrl100 = trackDataDto.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: "",
            previewUrl = trackDataDto.previewUrl ?: "",
            collectionName = trackDataDto.collectionName ?: "nothing found",
            releaseDateFormated = formatReleaseDate(trackDataDto.releaseDate),
            primaryGenreName = trackDataDto.primaryGenreName?: "nothing found",
            country = trackDataDto.country ?: "nothing found",
            isFavorite = isFavorite
        )
    }

    private fun formatReleaseDate(releaseDate: Date?): String? {

        if (releaseDate == null) return "${Date()}"
        return try {
            SimpleDateFormat("yyyy", Locale.getDefault()).format(releaseDate)
        } catch (e: Exception) {
            "${Date()}"
        }
    }


    override fun reversedMap(trackData: TrackData): TrackDataDto {
        return TrackDataDto(
            trackId = trackData.trackId,
            trackName = trackData.trackName,
            artistName = trackData.artistName,
            trackTimeMillis = parseMinutesAndSecondsToMillis(trackData.trackFormatedTime ?: "0:00"),
            artworkUrl100 = trackData.formatedArtworkUrl100?.replaceAfterLast('/', "100x100bb.jpg"),
            previewUrl = trackData.previewUrl,
            collectionName = trackData.collectionName,
            releaseDate = parseReleaseDate(trackData.releaseDateFormated),
            primaryGenreName = trackData.primaryGenreName,
            country = trackData.country
        )
    }

    private fun parseReleaseDate(releaseDateFormated: String?): Date {
        if (releaseDateFormated.isNullOrEmpty() || releaseDateFormated == "-") {
            return Date()
        }
        return try {
            SimpleDateFormat("yyyy", Locale.getDefault()).parse(releaseDateFormated) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }


}