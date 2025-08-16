package com.practicum.playlist_maker_one.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
    private val sharedPrefs: SharedPrefsTrack
) : ViewModel() {

    private var lastSearchText: String? = null

    private var searchJob : Job? = null
    //последний state
    private var lastState: SearchState = SearchState.History(emptyList())

    // текст запроса
    private var savedQuery: String = ""

    private var flagHistory : Boolean = true

    private var stateLiveData = MutableLiveData<SearchState>(SearchState.History(trackManager.getTrackHistory().map { mapper.map(it) }))
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

    private fun search(changedText: String) {
        savedQuery = changedText
        if (changedText.isEmpty()) {
            listOfSongs.clear()
            renderState(SearchState.History(trackManager.getTrackHistory().map { mapper.map(it) }))
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

    fun loadHistory(){
        renderState(SearchState.History(trackManager.getTrackHistory().map { mapper.map(it) }))
    }

    fun historyClear(){
        trackManager.deliteHistory()
        sharedPrefs.saveHistory(trackManager.getTrackHistory())
        flagHistory = false
        renderState(SearchState.History(trackManager.getTrackHistory().map { mapper.map(it) }))
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