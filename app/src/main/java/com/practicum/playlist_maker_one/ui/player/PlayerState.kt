package com.practicum.playlist_maker_one.ui.player

sealed interface PlayerState{
    object Playing : PlayerState
    object Paused : PlayerState
    object Finished : PlayerState
}