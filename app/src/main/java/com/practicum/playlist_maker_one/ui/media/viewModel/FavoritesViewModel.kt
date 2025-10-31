package com.practicum.playlist_maker_one.ui.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.domain.db.LikedHistoryInteractor
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.media.FavoritesState
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val likedHistoryInteractor: LikedHistoryInteractor
) : ViewModel() {

    private var stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState() : LiveData<FavoritesState> = stateLiveData


    fun loadLikedTracks(){

        viewModelScope.launch{
            try {
                likedHistoryInteractor.getLikedTracks().collect { tracks ->
                    println("DEBUG: Interactor returned ${tracks.size} tracks")
                    if (tracks.isEmpty()) {
                        stateLiveData.postValue(FavoritesState.NothingFound)
                    } else {
                        stateLiveData.postValue(FavoritesState.Content(tracks))
                    }
                }
            } catch (e: Exception) {
                stateLiveData.postValue(FavoritesState.NothingFound)
            }
        }
    }
}