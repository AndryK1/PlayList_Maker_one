package com.practicum.playlist_maker_one.ui.search

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker_one.Creator
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.api.trackPlayer
import com.practicum.playlist_maker_one.domain.impl.TrackPlayerImpl
import java.text.SimpleDateFormat
import java.util.Locale

class AudioActivity : AppCompatActivity() {

    private lateinit var pauseButton : ImageButton
    private val trackManager = Creator.getTrackManager()
    private val handler = Handler(Looper.getMainLooper())
    private val player = Creator.getMediaPlayer()
    private lateinit var timerRunnable: Runnable
    private lateinit var tvTimeUnderPause : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        val track = Creator.getMapper().map(trackManager.getLastTrack())
        timerRunnable = timerManager()

        player.preparePlayer(track.previewUrl, timerRunnable)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val closeButton = findViewById<ImageButton>(R.id.closeButton)
        pauseButton = findViewById(R.id.pauseButton)
        val tvTrackName = findViewById<TextView>(R.id.trackName)
        val tvPoster = findViewById<ImageView>(R.id.poster)
        val artworkUrl : String = track.formatedArtworkUrl100
        val tvArtistName = findViewById<TextView>(R.id.artistText)
        val tvTrackTime = findViewById<TextView>(R.id.TimeText)
        tvTimeUnderPause = findViewById(R.id.timeUnderPause)
        val tvYear = findViewById<TextView>(R.id.YearText)
        val tvGenre = findViewById<TextView>(R.id.StyleText)
        val tvCountry = findViewById<TextView>(R.id.countryText)
        val tvAlbum = findViewById<TextView>(R.id.AlbumText)

        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvTrackTime.text = track.trackFormatedTime
        tvYear.text = track.releaseDateFormated
        tvGenre.text = track.primaryGenreName
        tvCountry.text = track.country
        tvAlbum.text = track.collectionName

        pauseButton.isEnabled = true
        pauseButton.setImageResource(R.drawable.ic_button_play_100)
        tvTimeUnderPause.text = formatTime(30)


        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerInside()
            .transform(RoundedCorners(8))
            .into(tvPoster)


        pauseButton.setOnClickListener{
            player.playbackControl(timerRunnable)
            buttonAppearance()
        }

        closeButton.setOnClickListener{
            finish()
        }


    }

    override fun onPause() {
        super.onPause()
        player.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.destroy()
    }

    private fun buttonAppearance(){
        if(player.getCurrentState()){
            pauseButton.setImageResource(R.drawable.ic_pause_button_100)
        }
        else{
            pauseButton.setImageResource(R.drawable.ic_button_play_100)
        }
    }

    private fun timerManager(): Runnable {
        return object : Runnable {
            override fun run() {
                val currentSeconds = player.getSecondsRemain()
                tvTimeUnderPause.text = formatTime(currentSeconds)

                if (currentSeconds > 0 && player.getCurrentState()) {
                    handler.postDelayed(this, DELAYED)
                }
                else if(!player.getCurrentState()){
                    tvTimeUnderPause.text = formatTime(currentSeconds)
                    pauseButton.setImageResource(R.drawable.ic_button_play_100)
                }
                else{
                    tvTimeUnderPause.text = formatTime(30)
                    pauseButton.setImageResource(R.drawable.ic_button_play_100)
                }
            }
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }


}