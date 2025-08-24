package com.practicum.playlist_maker_one.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName ="track_table")
data class TrackEntity (
    @PrimaryKey(autoGenerate = false)
    val id : Long,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val previewUrl: String, // Ссылка на 30-ти секундный отрывок трека
    val collectionName: String,//альбом
    val releaseDate: Long,//дата выхода трека
    val primaryGenreName: String,//жанр
    val country: String//старана
)