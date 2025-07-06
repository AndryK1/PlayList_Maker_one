package com.practicum.playlist_maker_one.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.practicum.playlist_maker_one.databinding.FragmentPlayListBinding
import com.practicum.playlist_maker_one.ui.media.viewModel.PlayListViewModel


class FragmentPlayList : Fragment() {

    private var _binding : FragmentPlayListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setupObservers() {
        //тут будет подписка на данные
    }
}