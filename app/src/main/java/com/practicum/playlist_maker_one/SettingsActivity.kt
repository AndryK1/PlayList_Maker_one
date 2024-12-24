package com.practicum.playlist_maker_one

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<RelativeLayout>(R.id.list2).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    (R.string.linkDevelopment)
                )
            }
            startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
        }

        findViewById<RelativeLayout>(R.id.list3).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, (R.string.mail))
                putExtra(Intent.EXTRA_SUBJECT, (R.string.subjectMessage))
                putExtra(Intent.EXTRA_TEXT,(R.string.gratefulMessage))
            }
            startActivity(intent)
        }

        findViewById<RelativeLayout>(R.id.list4).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.linkAgreement))
            }
            startActivity(intent)
        }

        val closeButton = findViewById<ImageButton>(R.id.button1)
        closeButton.setOnClickListener{
            finish()
        }

    }

}