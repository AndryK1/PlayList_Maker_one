package com.practicum.playlist_maker_one.di

import android.app.Application
import androidx.core.content.ContextCompat.getString
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.ui.media.viewModel.CreateListViewModel
import com.practicum.playlist_maker_one.ui.media.viewModel.FavoritesViewModel
import com.practicum.playlist_maker_one.ui.media.viewModel.PlayListViewModel
import com.practicum.playlist_maker_one.ui.player.view_model.AudioViewModel
import com.practicum.playlist_maker_one.ui.search.view_model.SearchViewModel
import com.practicum.playlist_maker_one.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel {
        AudioViewModel(get(), get(), get())
    }

    viewModel{
        SearchViewModel(get(), get(), get(), get())
    }

    viewModel{
        val app = get<Application>()

        SettingsViewModel(
            app.getString(R.string.linkDevelopment),
            app.getString(R.string.mail),
            app.getString(R.string.subjectMessage),
            app.getString(R.string.gratefulMessage),
            app.getString(R.string.linkAgreement),
            get()
        )
    }

    viewModel{
        FavoritesViewModel(get())
    }

    viewModel{
        CreateListViewModel(get())
    }

    viewModel{
        PlayListViewModel(get())
    }
}
