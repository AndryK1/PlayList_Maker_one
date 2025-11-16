package com.practicum.playlist_maker_one.data.dto

import java.util.Date

data class TrackDataDto(val trackId: Long,
                        val trackName: String, // Название композиции
                        val artistName: String?, // Имя исполнителя
                        val trackTimeMillis: Int?, // Продолжительность трека
                        val artworkUrl100: String?, // Ссылка на изображение обложки
                        val previewUrl: String?, // Ссылка на 30-ти секундный отрывок трека
                        val collectionName: String?,//альбом
                        val releaseDate: Date? ,//дата выхода трека
                        val primaryGenreName: String?,//жанр
                        val country: String?,//стараны
                        )
