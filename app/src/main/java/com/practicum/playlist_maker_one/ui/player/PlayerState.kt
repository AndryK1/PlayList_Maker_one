package com.practicum.playlist_maker_one.ui.player

sealed class PlayerState{
    object Playing : PlayerState()
    object Paused : PlayerState()
    object Finished : PlayerState()
}