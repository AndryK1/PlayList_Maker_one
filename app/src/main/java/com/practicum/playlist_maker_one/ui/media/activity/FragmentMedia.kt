package com.practicum.playlist_maker_one.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.media.viewModel.FavoritesViewModel
import com.practicum.playlist_maker_one.ui.media.viewModel.PlayListViewModel
import com.practicum.playlist_maker_one.ui.player.activity.AudioFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentMedia : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private val playListViewModel: PlayListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MediaActivity(
                    favoritesViewModel = favoritesViewModel,
                    playListViewModel = playListViewModel,
                    onTrackClick = { track ->
                        navigateToPlayer(track)
                    },
                    onNewPlayListClick = {
                        navigateToCreateList()
                    },
                    onPlayListClick = { playList ->
                        navigateToCurrentPlaylist(playList)
                    }
                )
            }
        }
    }

    private fun navigateToPlayer(track: TrackData) {
        findNavController().navigate(
            R.id.action_fragmentMedia_to_audioFragment,
            AudioFragment.createTrack(track)
        )
    }

    private fun navigateToCreateList() {
        findNavController().navigate(R.id.action_fragmentMedia_to_fragmentCreateList)
    }

    private fun navigateToCurrentPlaylist(playList: PlayListData) {
        findNavController().navigate(
            R.id.action_fragmentMedia_to_fragmentCurrentPlaylist,
            FragmentCurrentPlaylist.createPlaylist(playList)
        )
    }
}