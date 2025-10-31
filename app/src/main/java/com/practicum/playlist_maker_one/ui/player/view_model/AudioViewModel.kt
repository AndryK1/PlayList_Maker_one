package com.practicum.playlist_maker_one.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.domain.api.TrackPlayer
import com.practicum.playlist_maker_one.domain.db.LikedHistoryInteractor
import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.player.PlayerState
import com.practicum.playlist_maker_one.ui.player.service.MusicService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class AudioViewModel(
    private val player : TrackPlayer,
    private val likedHistoryInteractor: LikedHistoryInteractor,
    private val playlistInteractor : PlayListInteractor
    ) : ViewModel(){

    private var musicService: MusicService? = null

    private var isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavorite() : LiveData<Boolean> = isFavoriteLiveData

    private var playlistsLiveData = MutableLiveData<List<PlayListData>>()
    fun observeList() : LiveData<List<PlayListData>> = playlistsLiveData

    private var lastTrackLiveData = MutableLiveData<TrackData>()
    fun observeLastTrack() : LiveData<TrackData> = lastTrackLiveData

    fun prepare(previewUrl: String, time: String) {
        musicService?.initializePlayer(player)
        musicService?.prepare(previewUrl, time)
    }

    suspend fun onLikeClicked(track : TrackData){
        val currentState = isFavoriteLiveData.value ?: false

        isFavoriteLiveData.postValue(likedHistoryInteractor.invoke(track, currentState))
    }

    fun loadLikeState(trackData: TrackData){
        viewModelScope.launch {
            val isFavorite = likedHistoryInteractor.isTrackFavorite(trackData)
            isFavoriteLiveData.postValue(isFavorite)
        }
    }

    fun initializeService(service: MusicService){
        this.musicService = service
    }

    fun getLastTrack(lastTrack: TrackData?){
        if(lastTrack != null){
            lastTrackLiveData.postValue(lastTrack)
        }
    }

    fun play(){
        musicService?.play()
    }

    fun onPausePlayer(){
        musicService?.onPausePlayer()
    }

    fun onDestroyPlayer(){
        musicService?.onDestroyPlayer()
    }

    fun loadPlayLists(){
        viewModelScope.launch {
            playlistInteractor.getPlayLists().collect { listData ->
                playlistsLiveData.postValue(listData)
            }
        }
    }

    companion object{
        const val DELAYED = 1000L
    }
}