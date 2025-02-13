package com.practicum.playlist_maker_one

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        val track = TrackHistoryManager.getLastTrack()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val closeButton = findViewById<ImageButton>(R.id.closeButton)
        val tvTrackName = findViewById<TextView>(R.id.trackName)
        val tvPoster = findViewById<ImageView>(R.id.poster)
        val artworkUrl : String = TrackHistoryManager.getCoverArtwork(track)
        val tvArtistName = findViewById<TextView>(R.id.artistText)
        val tvTrackTime = findViewById<TextView>(R.id.TimeText)
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



        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerInside()
            .transform(RoundedCorners(8))
            .into(tvPoster)


        closeButton.setOnClickListener{
            finish()
        }



    }
}
