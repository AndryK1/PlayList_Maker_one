package com.practicum.playlist_maker_one.ui.settings.view_model

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.api.ThemeManager
import com.practicum.playlist_maker_one.ui.settings.SettingsState
import com.practicum.playlist_maker_one.ui.track.App
import com.practicum.playlist_maker_one.util.Creator

// тут сделал передачу ресурсов из string как было в блоке работа с ViewModel, но я не уверен верно или нет,слишком много всего в конструкторе и получается viewModel также берёт на себя хранение строк, если нужно по другому, подскажите ,как лучше сделать
class SettingsViewModel(
    private val context: Context,
    private val prefixShare: String,
    private val prefixEmail: String,
    private val prefixSubjectMessage: String,
    private val prefixGratefulMessage: String,
    private val prefixUrl: String
) : ViewModel() {
    private val sharedPrefs = Creator.getThemeManager()

    companion object{
        fun getFactory() : ViewModelProvider.Factory = viewModelFactory {
            initializer{
                val app = (this[APPLICATION_KEY] as App)
                SettingsViewModel(app,
                    app.getString(R.string.linkDevelopment),
                    app.getString(R.string.mail),
                    app.getString(R.string.subjectMessage),
                    app.getString(R.string.gratefulMessage),
                    app.getString(R.string.linkAgreement)
                )
            }
        }
    }

    private val nightStateLiveData = MutableLiveData<Boolean>(sharedPrefs.isDarkThemeEnabled(context))
    fun observeState() : LiveData<Boolean> = nightStateLiveData

    private val sharingStateLiveData = MutableLiveData<SettingsState>()
    fun observeSharingState() : LiveData<SettingsState> = sharingStateLiveData

    fun switchDarkTheme(enabled: Boolean){
        sharedPrefs.setDarkTheme(enabled, context)
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