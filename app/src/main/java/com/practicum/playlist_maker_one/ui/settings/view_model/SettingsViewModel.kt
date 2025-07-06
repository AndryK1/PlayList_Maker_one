package com.practicum.playlist_maker_one.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.api.SharedPrefsTrack
import com.practicum.playlist_maker_one.domain.api.ThemeManager
import com.practicum.playlist_maker_one.ui.settings.SettingsState
import com.practicum.playlist_maker_one.ui.track.App

class SettingsViewModel(
    private val prefixShare: String,
    private val prefixEmail: String,
    private val prefixSubjectMessage: String,
    private val prefixGratefulMessage: String,
    private val prefixUrl: String,
    private val sharedPrefs : ThemeManager
) : ViewModel() {

    private val nightStateLiveData = MutableLiveData<Boolean>(sharedPrefs.isDarkThemeEnabled())
    fun observeState() : LiveData<Boolean> = nightStateLiveData

    private val sharingStateLiveData = MutableLiveData<SettingsState>()
    fun observeSharingState() : LiveData<SettingsState> = sharingStateLiveData

    fun switchDarkTheme(enabled: Boolean){
        sharedPrefs.setDarkTheme(enabled)
        nightStateLiveData.postValue(enabled)
    }

    fun onShareClicked() {
        renderState(SettingsState.Share(prefixShare))
    }

    fun onSupportClicked(){
        renderState(SettingsState.Support(
            prefixEmail,
            prefixSubjectMessage,
            prefixGratefulMessage
        ))
    }

    fun onAgreementClicked(){
        renderState(SettingsState.Agreement(prefixUrl))
    }


    private fun renderState(state: SettingsState) {
        sharingStateLiveData.postValue(state)
    }

}