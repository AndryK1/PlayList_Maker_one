package com.practicum.playlist_maker_one

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.VibratorManager
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale

class AudioActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var pauseButton : ImageButton
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var timerRunnable: Runnable
    private lateinit var tvTimeUnderPause : TextView
    private var secondsRemain : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        val track = TrackHistoryManager.getLastTrack()

        preparePlayer(track.previewUrl)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val closeButton = findViewById<ImageButton>(R.id.closeButton)
        pauseButton = findViewById(R.id.pauseButton)
        val tvTrackName = findViewById<TextView>(R.id.trackName)
        val tvPoster = findViewById<ImageView>(R.id.poster)
        val artworkUrl : String = TrackHistoryManager.getCoverArtwork(track)
        val tvArtistName = findViewById<TextView>(R.id.artistText)
        val tvTrackTime = findViewById<TextView>(R.id.TimeText)
        tvTimeUnderPause = findViewById(R.id.timeUnderPause)
        val tvYear = findViewById<TextView>(R.id.YearText)
        val tvGenre = findViewById<TextView>(R.id.StyleText)
        val tvCountry = findViewById<TextView>(R.id.countryText)
        val tvAlbum = findViewById<TextView>(R.id.AlbumText)

        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        tvYear.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(track.releaseDate)
        tvGenre.text = track.primaryGenreName
        tvCountry.text = track.country
        tvAlbum.text = track.collectionName

        timerRunnable = timerManager()


        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerInside()
            .transform(RoundedCorners(8))
            .into(tvPoster)


        pauseButton.setOnClickListener{
            playbackControl()
        }

        closeButton.setOnClickListener{
            finish()
        }


    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer( trackUrl: String){
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            pauseButton.isEnabled = true
            playerState = STATE_PREPARED
            setRemainTime()
        }

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler.removeCallbacks(timerRunnable)
            pauseButton.setImageResource(R.drawable.ic_button_play_100)
            setRemainTime()
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        pauseButton.setImageResource(R.drawable.ic_pause_button_100)
        playerState = STATE_PLAYING
    }

    private fun setRemainTime() {
        if(secondsRemain == 0){
            secondsRemain = 30
            tvTimeUnderPause.text = formatTime(secondsRemain)
        }
    }

    private fun timerManager(): Runnable {
        return object : Runnable {
            override fun run() {
                tvTimeUnderPause.text = formatTime(secondsRemain)
                if(secondsRemain > 0){
                    secondsRemain--
                }
                handler.postDelayed(this, DELAYED)
            }
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        pauseButton.setImageResource(R.drawable.ic_button_play_100)
        playerState = STATE_PAUSED
    }

    private fun playbackControl(){
        when(playerState){
            STATE_PLAYING->{
                pausePlayer()
                handler.removeCallbacks(timerRunnable)
            }
            STATE_PAUSED, STATE_PREPARED->{
                startPlayer()
                handler.post(timerRunnable)
            }
        }
    }

}