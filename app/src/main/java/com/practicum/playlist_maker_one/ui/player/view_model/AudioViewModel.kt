package com.practicum.playlist_maker_one.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker_one.ui.player.PlayerState
import com.practicum.playlist_maker_one.ui.search.activity.DELAYED
import com.practicum.playlist_maker_one.util.Creator

class AudioViewModel(previewUrl: String) : ViewModel(){

    private val handler = Handler(Looper.getMainLooper())
    private val player = Creator.getMediaPlayer()
    private var timerRunnable: Runnable = timerManager()

    private var playingLiveData = MutableLiveData<PlayerState>()
    fun observePlayer() : LiveData<PlayerState> = playingLiveData

    private var timerLiveData = MutableLiveData<String>("00:30")
    fun observeTimer() : LiveData<String> = timerLiveData

    companion object {
        fun getFactory(url: String) : ViewModelProvider.Factory = viewModelFactory{
            initializer {
                AudioViewModel(url)
            }
        }
    }

    init {
        player.preparePlayer(previewUrl, timerRunnable)
        player.setOnCompletionListener {
            playingLiveData.postValue(PlayerState.Finished)
            timerLiveData.postValue("00:30")
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