package com.practicum.playlist_maker_one.ui.media

import com.practicum.playlist_maker_one.domain.entity.TrackData

sealed interface FavoritesState {

    object NothingFound : FavoritesState

    data class Content(
        val tracks : List<TrackData>
    ) : FavoritesState
}