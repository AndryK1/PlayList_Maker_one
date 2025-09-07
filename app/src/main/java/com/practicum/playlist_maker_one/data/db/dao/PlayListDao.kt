package com.practicum.playlist_maker_one.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlist_maker_one.data.db.PlayListEntity

@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList : PlayListEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlayLists() : List<PlayListEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getCurrentPlayList(id: Long) : PlayListEntity

    @Update
    suspend fun updatePlayList(playList: PlayListEntity)
}