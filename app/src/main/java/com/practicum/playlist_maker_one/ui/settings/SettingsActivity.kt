package com.practicum.playlist_maker_one.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlist_maker_one.Creator
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.ui.track.APP_PREFERENCES

class SettingsActivity : AppCompatActivity() {
    private val sharedPrefs = Creator.getThemeManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.darkTheme)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //тёмная тема

        val isNightModeEnabled = sharedPrefs.isDarkThemeEnabled(this)
        themeSwitch.isChecked = isNightModeEnabled


        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefs.setDarkTheme(true,this)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefs.setDarkTheme(false,this)
            }
        }

        //
        findViewById<MaterialTextView>(R.id.share).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.linkDevelopment)
                )
            }
            startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
        }

        findViewById<MaterialTextView>(R.id.support).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.mail))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subjectMessage))
                putExtra(Intent.EXTRA_TEXT,getString(R.string.gratefulMessage))
            }
            startActivity(intent)
        }

        findViewById<MaterialTextView>(R.id.usersAgree).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.linkAgreement))
            }
            startActivity(intent)
        }

        findViewById<MaterialToolbar>(R.id.toolbarSettings).setNavigationOnClickListener {
            finish()
        }

    }

}