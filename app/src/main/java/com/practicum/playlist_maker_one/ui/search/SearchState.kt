package com.practicum.playlist_maker_one.ui.search

import com.practicum.playlist_maker_one.domain.entity.TrackData

sealed interface SearchState {

    object Loading : SearchState

    data class Content(
        val tracks : List<TrackData>
    ) : SearchState

    data class History(
        val history : List<TrackData>
    ) : SearchState

    object NothingFound : SearchState

    object InternetError : SearchState
}