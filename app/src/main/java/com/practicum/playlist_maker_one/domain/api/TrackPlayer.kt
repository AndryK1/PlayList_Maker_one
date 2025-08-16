package com.practicum.playlist_maker_one.domain.api

interface TrackPlayer {
    fun playbackControl(onTimerStart: () -> Unit)
    fun getCurrentState(): Boolean
    fun getSecondsRemain(): Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun preparePlayer( trackUrl: String, timerRunnable: Runnable)
    fun destroy()
    fun startPlayer()
    fun pausePlayer()
}