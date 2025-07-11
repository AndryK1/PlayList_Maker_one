package com.practicum.playlist_maker_one.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.api.TrackPlayer
import com.practicum.playlist_maker_one.ui.player.PlayerState
import com.practicum.playlist_maker_one.ui.track.App

class AudioViewModel(
                     private val handler: Handler,
                     private val player : TrackPlayer
    ) : ViewModel(){
        companion object{
            const val DELAYED = 1000L
        }
    private var timerRunnable: Runnable = timerManager()
    private lateinit var previewUrl: String
    private lateinit var startTime: String

    private var playingLiveData = MutableLiveData<PlayerState>()
    fun observePlayer() : LiveData<PlayerState> = playingLiveData

    private var timerLiveData = MutableLiveData<String>()
    fun observeTimer() : LiveData<String> = timerLiveData

    fun prepare(previewUrl : String, time: String) {

        this.previewUrl = previewUrl
        this.startTime = time
        player.preparePlayer(previewUrl, timerRunnable)
        player.setOnCompletionListener {
            playingLiveData.postValue(PlayerState.Finished)
            timerLiveData.postValue(startTime)
        }
    }

    fun play(){
        player.playbackControl(timerRunnable)

        if (player.getCurrentState()) {
            playingLiveData.postValue(PlayerState.Playing)
        } else {
            playingLiveData.postValue(PlayerState.Paused)
        }
    }


    fun onPausePlayer(){
        player.pausePlayer()
    }

    fun onDestroyPlayer(){
        player.destroy()
    }

    private fun timerManager(): Runnable {
        return object : Runnable {
            override fun run() {
                val currentSeconds = player.getSecondsRemain()
                timerLiveData.postValue(formatTime(currentSeconds))

                if (currentSeconds > 0 && player.getCurrentState()) {
                    handler.postDelayed(this, DELAYED)
                }

            }
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }


}