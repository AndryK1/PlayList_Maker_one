package com.practicum.playlist_maker_one.ui.media

import com.practicum.playlist_maker_one.domain.entity.PlayListData

sealed interface PlayListState {

    object NothingFound : PlayListState

    data class Content(
        val playLists: List<PlayListData>
    ) : PlayListState
}