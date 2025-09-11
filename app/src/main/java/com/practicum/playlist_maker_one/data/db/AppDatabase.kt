package com.practicum.playlist_maker_one.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlist_maker_one.data.db.dao.PlayListDao
import com.practicum.playlist_maker_one.data.db.dao.TrackDao

@Database(
    version = 3,
    entities = [
        TrackEntity::class,
        PlayListEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao() : TrackDao

    abstract fun playListDao() : PlayListDao
}