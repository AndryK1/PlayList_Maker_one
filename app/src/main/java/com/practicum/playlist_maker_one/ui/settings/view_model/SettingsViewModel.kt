package com.practicum.playlist_maker_one.ui.settings.view_model

import androidx.appcompat.app.AppCompatDelegate
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val prefixShare: String,
    private val prefixEmail: String,
    private val prefixSubjectMessage: String,
    private val prefixGratefulMessage: String,
    private val prefixUrl: String,
    private val sharedPrefs : ThemeManager
) : ViewModel() {


    private val _nightStateLiveData = MutableStateFlow(sharedPrefs.isDarkThemeEnabled())
    var nightStateLiveData : StateFlow<Boolean> = _nightStateLiveData.asStateFlow()

    private val _sharingStateLiveData: MutableStateFlow<SettingsState?> = MutableStateFlow(null)
    var sharingStateLiveData : StateFlow<SettingsState?> = _sharingStateLiveData.asStateFlow()

    fun switchDarkTheme(enabled: Boolean){
        sharedPrefs.setDarkTheme(enabled)
        _nightStateLiveData.value = enabled

        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
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

    fun resetSharingState() {
        _sharingStateLiveData.value = null
    }

    private fun renderState(state: SettingsState) {
        _sharingStateLiveData.value = (state)
    }

}