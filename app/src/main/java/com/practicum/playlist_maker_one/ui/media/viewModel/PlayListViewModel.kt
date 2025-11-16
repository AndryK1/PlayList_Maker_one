package com.practicum.playlist_maker_one.ui.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.ui.media.PlayListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayListViewModel(private val interactor: PlayListInteractor) : ViewModel() {

    private val _stateLiveData = MutableStateFlow<PlayListState?>(null)
    var stateLiveData : StateFlow<PlayListState?> = _stateLiveData.asStateFlow()

    fun loadPlaylists(){
        viewModelScope.launch{
            interactor.getPlayLists().collect { list ->
                if(list.isEmpty()){
                    _stateLiveData.value = PlayListState.NothingFound
                }
                else{
                    _stateLiveData.value = PlayListState.Content(
                        list
                    )
                }
            }
        }
    }

}