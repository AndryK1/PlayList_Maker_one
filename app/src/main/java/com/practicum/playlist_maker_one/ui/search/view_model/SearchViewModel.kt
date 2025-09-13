package com.practicum.playlist_maker_one.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.data.dto.TrackDataDto
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack
import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.domain.useCase.TrackRepositoryInteractor
import com.practicum.playlist_maker_one.ui.search.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackManager : TrackHistoryManager,
    private var trackInteractor: TrackRepositoryInteractor,
    private val mapper : TrackMapper,
    private val sharedPrefs: SharedPrefsTrack,
    private val history: TrackHistoryManager
) : ViewModel() {

    private var lastSearchText: String? = null

    private var searchJob : Job? = null
    //последний state
    private var lastState: SearchState = SearchState.History(emptyList())

    // текст запроса
    private var savedQuery: String = ""

    private var flagHistory : Boolean = true

    private var stateLiveData = MutableLiveData<SearchState>()
    fun observeState() : LiveData<SearchState> = stateLiveData

    private var listOfSongs: ArrayList<TrackData> = ArrayList()

    init{
        trackManager.initializeHistory()
        loadHistory()
    }

    fun getLastQuery(): String = savedQuery

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText && stateLiveData.value != SearchState.InternetError) {
            return
        }

        this.lastSearchText = changedText

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(changedText)
        }
    }

    fun onTrackClicked(track: TrackData){
        history.addTrackToHistory(mapper.reversedMap(track))
        sharedPrefs.saveHistory(history.getTrackHistory())
    }

    private fun search(changedText: String) {
        savedQuery = changedText
        if (changedText.isEmpty()) {
            listOfSongs.clear()
            loadHistory()
            flagHistory = true

        } else {

            viewModelScope.launch {
                renderState(SearchState.Loading)
                flagHistory = false
                trackInteractor
                trackInteractor
                    .searchTracks(changedText)
                    .collect { pair ->
                        when {

                            pair.second != null -> {
                                renderState(SearchState.InternetError)
                            }
                            pair.first?.isEmpty() == true -> {
                                renderState(SearchState.NothingFound)
                            }
                            pair.first != null -> {
                                renderState(SearchState.Content(pair.first!!.toList()))
                            }
                            else -> {
                                renderState(SearchState.InternetError)
                            }
                        }
                    }

            }
            }


    }

    fun loadHistory() {
        viewModelScope.launch {
            val history = trackManager.getTrackHistory().map { track ->
                val isFavorite = checkFavorite(track)
                mapper.map(track, isFavorite)
            }
            renderState(SearchState.History(history))
        }
    }

    fun historyClear(){
        trackManager.deleteHistory()
        sharedPrefs.saveHistory(trackManager.getTrackHistory())
        flagHistory = false
        loadHistory()
    }

    private suspend fun checkFavorite(trackData: TrackDataDto): Boolean {
        return trackManager.getFavorites(trackData)
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getLastState(): SearchState = lastState

    private fun renderState(state: SearchState) {
        lastState = state
        stateLiveData.postValue(state)
    }


    companion object{
        private const val SEARCH_DEBOUNCE_DELAY = 1000L

    }

}