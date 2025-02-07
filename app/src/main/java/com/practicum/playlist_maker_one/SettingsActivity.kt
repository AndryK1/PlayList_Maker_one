package com.practicum.playlist_maker_one

import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.darkTheme)
        sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //тёмная тема

        val isNightModeEnabled = sharedPrefs.getBoolean(DARK_THEME_KEY, false)
        themeSwitch.isChecked = isNightModeEnabled


        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveAppTheme(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveAppTheme(false)
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
    private fun saveAppTheme(darkTheme : Boolean){
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_KEY, darkTheme)
            .apply()
    }

}