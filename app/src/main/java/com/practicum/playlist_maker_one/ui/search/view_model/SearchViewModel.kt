package com.practicum.playlist_maker_one.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack
import com.practicum.playlist_maker_one.domain.api.TrackHistoryManager
import com.practicum.playlist_maker_one.domain.api.TrackMapper
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.domain.useCase.TrackRepositoryInteractor
import com.practicum.playlist_maker_one.ui.search.SearchState

class SearchViewModel(
    private val trackManager : TrackHistoryManager,
    private var trackInteractor: TrackRepositoryInteractor,
    private val mapper : TrackMapper,
    private val handler: Handler,
    private val sharedPrefs: SharedPrefsTrack
) : ViewModel() {

    private var lastSearchText: String? = null

    private var stateLiveData = MutableLiveData<SearchState>(SearchState.History(trackManager.getTrackHistory().map { mapper.map(it) }))
    fun observeState() : LiveData<SearchState> = stateLiveData

    private var listOfSongs: ArrayList<TrackData> = ArrayList()

    init{
        trackManager.initializeHistory()
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText && stateLiveData.value != SearchState.InternetError) {
            return
        }

        this.lastSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { search(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun search(changedText: String) {
        if (changedText.isEmpty()) {
            listOfSongs.clear()
            renderState(SearchState.History(trackManager.getTrackHistory().map { mapper.map(it) }))

        } else {
            renderState(SearchState.Loading)
            trackInteractor.execute(changedText) { result ->
                result.onSuccess { trackList ->
                    listOfSongs.clear()
                        if (trackList.isNotEmpty() ) {
                            listOfSongs.addAll(trackList)
                            renderState(SearchState.Content(listOfSongs))
                        } else {
                            listOfSongs.clear()
                            renderState(SearchState.NothingFound)
                        }
                    }
                    result.onFailure {
                        renderState(SearchState.InternetError)
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
        renderState(SearchState.History(trackManager.getTrackHistory().map { mapper.map(it) }))
    }

    fun searchClear(){
        trackInteractor.destroy()
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        trackInteractor.destroy()
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    companion object{
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 1000L

    }

}