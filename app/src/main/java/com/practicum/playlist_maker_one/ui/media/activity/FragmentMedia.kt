package com.practicum.playlist_maker_one.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.FragmentMediaBinding
import com.practicum.playlist_maker_one.ui.media.viewModel.MediaViewModel

class FragmentMedia : Fragment() {
    private lateinit var binding: FragmentMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val viewModel: MediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pagerContainer.adapter = MediaPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.mediaTab, binding.pagerContainer) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.favorites)
                1 -> getString(R.string.playLists)
                else -> ""
            }
        }


        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        tabMediator.detach()
    }
}