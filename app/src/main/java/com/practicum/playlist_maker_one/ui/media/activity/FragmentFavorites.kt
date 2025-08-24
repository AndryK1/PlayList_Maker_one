package com.practicum.playlist_maker_one.ui.media.activity

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlist_maker_one.databinding.FragmentFavoritesBinding
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.media.FavoritesState
import com.practicum.playlist_maker_one.ui.media.TrackFavoritesAdapter
import com.practicum.playlist_maker_one.ui.media.viewModel.FavoritesViewModel
import com.practicum.playlist_maker_one.ui.player.activity.AudioFragment
import com.practicum.playlist_maker_one.ui.search.TrackAdapter
import com.practicum.playlist_maker_one.ui.search.TrackHistoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavorites : Fragment() {

    companion object {

        fun newInstance() = FragmentFavorites().apply{}

    }

    private var _binding : FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private lateinit var adapter : TrackFavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackFavoritesAdapter(
            onItemClick = { track -> navigateToPlayer(track) }, mutableListOf()
        )

        val recyclerView = binding.recyclerViewFavorites
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        setupObservers()
        viewModel.loadLikedTracks()
    }

    private fun setupObservers() {
        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }
    }


    private fun render(state: FavoritesState){
        when(state){
            is FavoritesState.Content -> {
                binding.recyclerViewFavorites.visibility = View.VISIBLE
                binding.nothingFoundImage.visibility = View.GONE
                binding.nothingFoundText.visibility = View.GONE
                adapter.updateData(state.tracks)

            }
            else -> {
                binding.recyclerViewFavorites.visibility = View.GONE
                binding.nothingFoundImage.visibility = View.VISIBLE
                binding.nothingFoundText.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToPlayer(track: TrackData){
        findNavController().navigate(
            com.practicum.playlist_maker_one.R.id.action_fragmentMedia_to_audioFragment,
            AudioFragment.createTrack(track))
    }


}