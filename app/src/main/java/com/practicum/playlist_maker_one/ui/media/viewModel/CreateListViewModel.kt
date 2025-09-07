package com.practicum.playlist_maker_one.ui.media.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import kotlinx.coroutines.launch

class CreateListViewModel(
    private val interactor: PlayListInteractor
) : ViewModel() {

        fun savePlayList(playListData: PlayListData){
         viewModelScope.launch{
             interactor.savePlayList(playListData)
         }
    }
}