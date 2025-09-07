package com.practicum.playlist_maker_one.ui.player.view_model

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class AudioViewModel(
    private val player : TrackPlayer,
    private val likedHistoryInteractor: LikedHistoryInteractor,
    private val playlistInteractor : PlayListInteractor
    ) : ViewModel(){

    var timerJob: Job?= null

    private lateinit var previewUrl: String
    private lateinit var startTime: String

    private var isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavorite() : LiveData<Boolean> = isFavoriteLiveData

    private var playingLiveData = MutableLiveData<PlayerState>()
    fun observePlayer() : LiveData<PlayerState> = playingLiveData

    private var timerLiveData = MutableLiveData<String>()
    fun observeTimer() : LiveData<String> = timerLiveData

    private var playlistsLiveData = MutableLiveData<List<PlayListData>>()
    fun observeList() : LiveData<List<PlayListData>> = playlistsLiveData

    private var lastTrackLiveData = MutableLiveData<TrackData>()
    fun observeLastTrack() : LiveData<TrackData> = lastTrackLiveData

    fun prepare(previewUrl: String, time: String) {
        this.previewUrl = previewUrl
        this.startTime = time
        player.preparePlayer(previewUrl) {
            playingLiveData.postValue(PlayerState.Finished)
            timerLiveData.postValue(startTime)
        }
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

    fun getLastTrack(lastTrack: TrackData?){
        if(lastTrack != null){
            lastTrackLiveData.postValue(lastTrack)
        }
    }

    fun play(){
        player.playbackControl(){
            startTimer()
        }

        if (player.getCurrentState()) {
            playingLiveData.postValue(PlayerState.Playing)
        } else {
            playingLiveData.postValue(PlayerState.Paused)
        }
    }

    private fun startTimer(){

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while(isActive && player.getCurrentState()){
                val currentSeconds = player.getSecondsRemain()
                timerLiveData.postValue(formatTime(currentSeconds))
                delay(DELAYED)
            }
        }
    }

    fun onPausePlayer(){
        player.pausePlayer()
    }

    fun onDestroyPlayer(){
        timerJob?.cancel()
        timerJob = null
        player.destroy()
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
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