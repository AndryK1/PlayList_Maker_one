package com.practicum.playlist_maker_one.domain.entity

import java.util.Date

data class TrackData(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackFormatedTime: String, // Продолжительность трека отформатированная
    val formatedArtworkUrl100: String, // Ссылка на изображение обложки отформатированная
    val previewUrl: String, // Ссылка на 30-ти секундный отрывок трека
    val collectionName: String,//альбом
    val releaseDateFormated: String,//дата выхода трека отформатированная
    val primaryGenreName: String,//жанр
    val country: String//старана

)
