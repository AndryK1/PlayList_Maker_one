package com.practicum.playlist_maker_one.ui.settings

sealed interface SettingsState {

    data class Share(val text: String) : SettingsState

    data class Support(val email: String, val subject: String, val text: String) : SettingsState

    data class Agreement(val url: String) : SettingsState

}