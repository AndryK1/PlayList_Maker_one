package com.practicum.playlist_maker_one.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlist_maker_one.databinding.FragmentFavoritesBinding
import com.practicum.playlist_maker_one.ui.media.viewModel.FavoritesViewModel

class FragmentFavorites : Fragment() {

    companion object {

        fun newInstance() = FragmentFavorites().apply{}

    }

    private var _binding : FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setupObservers() {
        //тут будет подписка на данные
    }



}