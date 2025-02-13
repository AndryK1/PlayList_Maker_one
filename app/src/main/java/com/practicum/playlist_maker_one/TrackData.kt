package com.practicum.playlist_maker_one
import java.util.Date

data class TrackData(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String,//альбом
    val releaseDate: Date,//дата выхода трека
    val primaryGenreName: String,//жанр
    val country: String//старана

)

