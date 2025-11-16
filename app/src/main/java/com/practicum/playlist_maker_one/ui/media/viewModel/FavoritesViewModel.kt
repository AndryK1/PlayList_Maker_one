package com.practicum.playlist_maker_one.ui.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.domain.db.LikedHistoryInteractor
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.media.FavoritesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val likedHistoryInteractor: LikedHistoryInteractor
) : ViewModel() {

    private val _stateLiveData = MutableStateFlow<FavoritesState?>(null)
    var stateLiveData : StateFlow<FavoritesState?> = _stateLiveData.asStateFlow()

    fun loadLikedTracks(){

        viewModelScope.launch{
            try {
                likedHistoryInteractor.getLikedTracks().collect { tracks ->
                    println("DEBUG: Interactor returned ${tracks.size} tracks")
                    if (tracks.isEmpty()) {
                        _stateLiveData.value = FavoritesState.NothingFound
                    } else {
                        _stateLiveData.value = FavoritesState.Content(tracks)
                    }
                }
            } catch (e: Exception) {
                _stateLiveData.value = FavoritesState.NothingFound
            }
        }
    }
}