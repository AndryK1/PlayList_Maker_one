package com.practicum.playlist_maker_one.ui.media.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.ActivityMediaBinding
import com.practicum.playlist_maker_one.ui.media.viewModel.MediaViewModel

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val viewModel: MediaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.pagerContainer.adapter = MediaPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.mediaTab, binding.pagerContainer) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.favorites)
                1 -> getString(R.string.playList)
                else -> ""
            }
        }

        binding.toolbarMedia.setOnClickListener{
            finish()
        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}