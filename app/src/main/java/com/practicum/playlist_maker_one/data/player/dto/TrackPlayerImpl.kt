package com.practicum.playlist_maker_one.data.player.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlist_maker_one.domain.api.TrackPlayer


class TrackPlayerImpl : TrackPlayer{

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var secondsRemain : Int = 30
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying : Boolean = false
    private var completionListener: (() -> Unit)? = null// () -> Unit аналог void

    private var remainingTime : Int = 0


     override fun preparePlayer( trackUrl: String, timerRunnable: Runnable){
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            isPlaying = false
            playerState = STATE_PREPARED
            secondsRemain = (mediaPlayer.duration / 1000).coerceAtMost(30)//возвращает само число в диапазоне, либо максимум
        }

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            isPlaying = false
            handler.removeCallbacks(timerRunnable)
            secondsRemain = (mediaPlayer.duration / 1000).coerceAtMost(30)

            completionListener?.invoke()
        }
    }
    override fun startPlayer() {
        mediaPlayer.start()
        isPlaying = true
        playerState = STATE_PLAYING
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        completionListener = listener
    }

    override fun getSecondsRemain() : Int{
        return if (isPlaying) {
            remainingTime = (mediaPlayer.duration - mediaPlayer.currentPosition) / 1000
            remainingTime.coerceIn(0, 30)//в диапазоне от и до
        } else {
            remainingTime
        }
    }

    override fun getCurrentState() : Boolean{
        return isPlaying
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        isPlaying = false
        playerState = STATE_PAUSED
    }

    override fun playbackControl(timerRunnable: Runnable) {
        when(playerState){
            STATE_PLAYING ->{
                pausePlayer()
                handler.removeCallbacks(timerRunnable)
            }
            STATE_PAUSED, STATE_PREPARED ->{
                startPlayer()
                handler.post(timerRunnable)
            }
        }
    }

    override fun destroy(){
        mediaPlayer.release()
    }

    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}