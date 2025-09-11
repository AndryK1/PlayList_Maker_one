package com.practicum.playlist_maker_one.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlist_maker_one.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.practicum.playlist_maker_one.databinding.FragmentPlayListBinding
import com.practicum.playlist_maker_one.domain.db.PlayListInteractor
import com.practicum.playlist_maker_one.domain.entity.PlayListData
import com.practicum.playlist_maker_one.ui.media.PlayListAdapter
import com.practicum.playlist_maker_one.ui.media.PlayListState
import com.practicum.playlist_maker_one.ui.media.viewModel.PlayListViewModel
import com.practicum.playlist_maker_one.ui.player.PlayerState
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentPlayList(
) : Fragment() {

    private var _binding : FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewPlaylist.layoutManager = GridLayoutManager(
            requireContext(),
            2
        )
        viewModel.loadPlaylists()

        setupObservers()

        binding.newPlayListButton.setOnClickListener {
            navigateToCreateList()
        }
    }
    private fun setupObservers() {
        viewModel.observeState().observe(viewLifecycleOwner){
            renderState(it)
        }
    }

    private fun renderState(state: PlayListState){
        when(state){
            is PlayListState.NothingFound -> {
                binding.recyclerViewPlaylist.visibility = View.GONE
                binding.nothingFoundText.visibility = View.VISIBLE
                binding.nothingFoundImage.visibility = View.VISIBLE
            }
            is PlayListState.Content -> {
                binding.recyclerViewPlaylist.visibility = View.VISIBLE
                binding.nothingFoundText.visibility = View.GONE
                binding.nothingFoundImage.visibility = View.GONE
                binding.recyclerViewPlaylist.adapter = PlayListAdapter(state.playLists,
                    onItemClick = {
                        navigateToCurrentPlaylist(playList = it)
                    })
            }
        }
    }

    private fun navigateToCreateList(){
        findNavController().navigate(R.id.action_fragmentMedia_to_fragmentCreateList)
    }

    private fun navigateToCurrentPlaylist( playList: PlayListData){
        findNavController().navigate(R.id.action_fragmentMedia_to_fragmentCurrentPlaylist,
            FragmentCurrentPlaylist.createPlaylist(playList))
    }

    companion object {

        fun newInstance() = FragmentPlayList().apply{}

    }
}