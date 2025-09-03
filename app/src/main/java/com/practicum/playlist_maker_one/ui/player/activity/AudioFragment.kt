package com.practicum.playlist_maker_one.ui.player.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.IntentCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.FragmentAudioPlayerBinding
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.player.PlayerState
import com.practicum.playlist_maker_one.ui.player.view_model.AudioViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class AudioFragment : Fragment() {

    companion object {
        private const val EXTRA_TRACK = "TRACK_EXTRA"

        fun createTrack(track: TrackData): Bundle = bundleOf(EXTRA_TRACK to track)

    }

    private val viewModel: AudioViewModel by viewModel()

    private lateinit var binding: FragmentAudioPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(EXTRA_TRACK, TrackData::class.java)
        } else {
            @Suppress("DEPRECATION")
            (arguments?.getParcelable(EXTRA_TRACK))
        }

        binding.timeUnderPause.text = getString(R.string.audioStartTime)


        if (track != null) {
            viewModel.prepare(track.previewUrl, getString(R.string.audioStartTime))
        }

        viewModel.observeFavorite().observe(viewLifecycleOwner){
            changeLikeState(it)
        }

        viewModel?.observePlayer()?.observe(viewLifecycleOwner ){
            when(it){
                is PlayerState.Playing -> {
                    binding.pauseButton.setImageResource(R.drawable.ic_pause_button_100)
                }
                is PlayerState.Paused -> {
                    binding.pauseButton.setImageResource(R.drawable.ic_button_play_100)
                }
                PlayerState.Finished -> {
                    binding.pauseButton.setImageResource(R.drawable.ic_button_play_100)
                    binding.timeUnderPause.text = getString(R.string.audioStartTime)
                }
            }
        }

        viewModel?.observeTimer()?.observe(viewLifecycleOwner){
            binding.timeUnderPause.text = it
        }

        val artworkUrl : String = track?.formatedArtworkUrl100 ?: ""

        viewModel.loadLikeState(track!!)

        track?.let{ safeTrack -> changeLikeState(safeTrack.isFavorite) }
        binding.trackName.text = track?.trackName
        binding.artistText.text = track?.artistName
        binding.TimeText.text = track?.trackFormatedTime
        binding.YearText.text = track?.releaseDateFormated
        binding.StyleText.text = track?.primaryGenreName
        binding.countryText.text = track?.country
        binding.AlbumText.text = track?.collectionName

        binding.pauseButton.isEnabled = true
        binding.pauseButton.setImageResource(R.drawable.ic_button_play_100)

        binding.closeButton.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.likeButton.setOnClickListener {
            track?.let { safeTrack ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.onLikeClicked(safeTrack)
                }
            }
        }

        val radius = getResources().getDimensionPixelSize(R.dimen.posterCornerRadius)

        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerInside()
            .transform(RoundedCorners(radius))
            .into(binding.poster)


        binding.pauseButton.setOnClickListener{
            viewModel?.play()
        }

    }

    private fun changeLikeState(state: Boolean){
        if(state){
            binding.likeButton.setImageResource(R.drawable.ic_liked_like_button_51)
        }
        else{
            binding.likeButton.setImageResource(R.drawable.button_like)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.onDestroyPlayer()
    }

}