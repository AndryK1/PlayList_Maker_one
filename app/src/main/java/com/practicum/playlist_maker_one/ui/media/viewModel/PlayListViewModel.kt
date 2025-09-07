package com.practicum.playlist_maker_one.ui.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.ui.media.PlayListState
import kotlinx.coroutines.launch

class PlayListViewModel(private val interactor: PlayListInteractor) : ViewModel() {

    private var stateLiveData = MutableLiveData<PlayListState>()
    fun observeState() : LiveData<PlayListState> = stateLiveData

    fun loadPlaylists(){
        viewModelScope.launch{
            interactor.getPlayLists().collect { list ->
                if(list.isEmpty()){
                    stateLiveData.postValue(PlayListState.NothingFound)
                }
                else{
                    stateLiveData.postValue(PlayListState.Content(
                        list
                    ))
                }
            }
        }
    }

}