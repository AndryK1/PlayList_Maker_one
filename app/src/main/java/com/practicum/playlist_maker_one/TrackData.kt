package com.practicum.playlist_maker_one

data class TrackData(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMills : Int, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)
