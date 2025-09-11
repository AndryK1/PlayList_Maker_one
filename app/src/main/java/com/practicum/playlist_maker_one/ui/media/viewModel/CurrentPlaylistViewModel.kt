package com.practicum.playlist_maker_one.ui.media.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CurrentPlaylistViewModel(
    private val interactor: PlayListInteractor
) : ViewModel() {

    private var playlistLiveData = MutableLiveData<PlayListData>()
    fun observeTracks() : LiveData<PlayListData> = playlistLiveData


    fun putPlaylist(playList: PlayListData){
        playlistLiveData.postValue(playList)
    }

    fun deleteTrack(trackData: TrackData){
        viewModelScope.launch{
            val newPlaylist = interactor.deleteTrackFromPlayList(trackData, playlistLiveData.value ?: return@launch)
            playlistLiveData.postValue(newPlaylist)
        }
    }

    fun deletePlaylist(playList: PlayListData){
        viewModelScope.launch{ interactor.deletePlaylist(playList) }
    }

    fun getGeneralTime(generalTime : List<String>) : Int{
        val time = generalTime.sumOf { time ->
            try{
                val parts = time.split(":")

                if(parts.size>=2){ parts[0].toInt() * 60 + parts[1].toInt() }else{ 0 }
            }
            catch (e: Exception){
                0
            }
        }

        return (time/ 60f).roundToInt()
    }
}