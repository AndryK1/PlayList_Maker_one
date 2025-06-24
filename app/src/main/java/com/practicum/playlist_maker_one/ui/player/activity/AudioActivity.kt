package com.practicum.playlist_maker_one.ui.player.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker_one.util.Creator
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.ActivityAudioPlayerBinding
import com.practicum.playlist_maker_one.domain.entity.TrackData
import com.practicum.playlist_maker_one.ui.player.PlayerState
import com.practicum.playlist_maker_one.ui.player.view_model.AudioViewModel

class AudioActivity : AppCompatActivity() {

    private var viewModel: AudioViewModel? = null

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = IntentCompat.getParcelableExtra(intent, EXTRA_TRACK, TrackData::class.java)

        if (track == null) {
            finish()
            return
        }

        viewModel = ViewModelProvider(this, AudioViewModel.getFactory(track.previewUrl)).get(AudioViewModel::class.java)


        viewModel?.observePlayer()?.observe(this){
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

        viewModel?.observeTimer()?.observe(this){
            binding.timeUnderPause.text = it
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val artworkUrl : String = track.formatedArtworkUrl100

        binding.trackName.text = track.trackName
        binding.artistText.text = track.artistName
        binding.TimeText.text = track.trackFormatedTime
        binding.YearText.text = track.releaseDateFormated
        binding.StyleText.text = track.primaryGenreName
        binding.countryText.text = track.country
        binding.AlbumText.text = track.collectionName

        binding.pauseButton.isEnabled = true
        binding.pauseButton.setImageResource(R.drawable.ic_button_play_100)

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

        binding.closeButton.setOnClickListener{
            finish()
        }


    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.onDestroyPlayer()
    }

    companion object {
        private const val EXTRA_TRACK = "TRACK_EXTRA"

        fun start(context: Context, track: TrackData) {
            val intent = Intent(context, AudioActivity::class.java).apply {
                putExtra(EXTRA_TRACK, track)
            }
            context.startActivity(intent)
        }
    }
}