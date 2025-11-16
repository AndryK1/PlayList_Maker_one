package com.practicum.playlist_maker_one.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.FragmentSearchBinding
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.main.activity.RootActivity
import com.practicum.playlist_maker_one.ui.player.activity.AudioFragment
import com.practicum.playlist_maker_one.ui.search.SearchState
import com.practicum.playlist_maker_one.ui.search.TrackAdapter
import com.practicum.playlist_maker_one.ui.search.TrackHistoryAdapter
import com.practicum.playlist_maker_one.ui.search.view_model.SearchViewModel

import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSearch : Fragment() {

    private val viewModel: SearchViewModel by viewModel(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                SearchActivity(
                    viewModel = viewModel,
                    onTrackClick = { track ->
                        navigateToPlayer(track)
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as RootActivity).registerInternetReceiver()
    }

    override fun onStop() {
        (requireActivity() as RootActivity).unregisterInternetReceiver()
        super.onStop()
    }


    private fun navigateToPlayer(track : TrackData){
        findNavController().navigate(R.id.action_fragmentSearch_to_audioFragment,
            AudioFragment.createTrack(track))
    }

}
