package com.practicum.playlist_maker_one.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlist_maker_one.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track : TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackEntity(track : TrackEntity)

    @Query(value = "SELECT id FROM  track_table WHERE id IN (:trackId)")
    suspend fun getFavoriteTrackIds(trackId : List<Long>) : List<Long>

    @Query(value = "SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query(value = "SELECT * FROM track_table WHERE id IN (:trackId)")
    suspend fun isTrackInFavorites(trackId: Long) : TrackEntity?
}