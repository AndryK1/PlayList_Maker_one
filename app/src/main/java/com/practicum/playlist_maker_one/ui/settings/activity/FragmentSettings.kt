package com.practicum.playlist_maker_one.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.FragmentSettingsBinding
import com.practicum.playlist_maker_one.ui.settings.SettingsState
import com.practicum.playlist_maker_one.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSettings : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            //стратегия для освобождения ресурсов
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {

                 SettingsActivity(viewModel = viewModel)
            }
        }
    }
}