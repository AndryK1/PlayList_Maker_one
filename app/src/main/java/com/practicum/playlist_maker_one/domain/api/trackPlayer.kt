package com.practicum.playlist_maker_one.domain.api

interface trackPlayer {
    fun playbackControl(timerRunnable: Runnable)
    fun getCurrentState(): Boolean
    fun getSecondsRemain(): Int
    fun preparePlayer( trackUrl: String, timerRunnable: Runnable)
    fun destroy()
    fun startPlayer()
    fun pausePlayer()
}