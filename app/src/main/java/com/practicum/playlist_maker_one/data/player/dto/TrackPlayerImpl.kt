package com.practicum.playlist_maker_one.data.player.dto

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlist_maker_one.domain.api.TrackPlayer


class TrackPlayerImpl(
) : TrackPlayer{

    private var playerState = STATE_DEFAULT
    private var secondsRemain : Int = 30
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying : Boolean = false
    private var isReleased : Boolean = false // чтобы в купе с bottomSheet не создавался бесконечный цикл ошибок
    private var completionListener: (() -> Unit)? = null// () -> Unit аналог void

    private var remainingTime : Int = 0


     override fun preparePlayer( trackUrl: String, timerRunnable: Runnable){
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()

        isReleased = false
        mediaPlayer?.setDataSource(trackUrl)
        mediaPlayer?.prepareAsync()

        mediaPlayer?.setOnPreparedListener {
            if(isReleased)return@setOnPreparedListener
            isPlaying = false
            playerState = STATE_PREPARED
            secondsRemain =
                (mediaPlayer?.duration?.div(1000))?.coerceAtMost(30) ?: 30//возвращает само число в диапазоне, либо максимум
        }

        mediaPlayer?.setOnCompletionListener {
            if(isReleased)return@setOnCompletionListener
            playerState = STATE_PREPARED
            isPlaying = false
            secondsRemain = (mediaPlayer?.duration?.div(1000))?.coerceAtMost(30) ?: 30

            completionListener?.invoke()
        }
    }
    override fun startPlayer() {
        mediaPlayer?.start()
        isPlaying = true
        playerState = STATE_PLAYING
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        completionListener = listener
    }

    override fun getSecondsRemain() : Int{
        if (isReleased) return remainingTime
        return if (isPlaying) {
            try {
                val duration = mediaPlayer?.duration ?: 0
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                remainingTime = (duration - currentPosition) / 1000
                remainingTime.coerceIn(0, 30)
            } catch (e: IllegalStateException) {
                remainingTime
            }
        } else {
            remainingTime
        }
    }

    override fun getCurrentState() : Boolean{
        return isPlaying
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        isPlaying = false
        playerState = STATE_PAUSED
    }

    override fun playbackControl(onTimerStart: () -> Unit) {
        when(playerState){
            STATE_PLAYING ->{
                pausePlayer()
            }
            STATE_PAUSED, STATE_PREPARED ->{
                startPlayer()
                onTimerStart()
            }
        }
    }

    override fun destroy(){
        isReleased = true
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}