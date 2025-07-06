package com.practicum.playlist_maker_one.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.ui.settings.SettingsState
import com.practicum.playlist_maker_one.ui.settings.view_model.SettingsViewModel


class SettingsActivity : AppCompatActivity() {
    private val viewModel : SettingsViewModel by viewModel()

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

        viewModel?.observeState()?.observe(this){
            if(it){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeSwitch.isChecked = it
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themeSwitch.isChecked = it
            }
        }

        viewModel?.observeSharingState()?.observe(this){
            render(it)
        }
        //тёмная тема

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel?.switchDarkTheme(isChecked)
        }

        //
        findViewById<MaterialTextView>(R.id.share).setOnClickListener {
           viewModel?.onShareClicked()
        }

        findViewById<MaterialTextView>(R.id.support).setOnClickListener {
            viewModel?.onSupportClicked()
        }

        findViewById<MaterialTextView>(R.id.usersAgree).setOnClickListener {
            viewModel?.onAgreementClicked()
        }

        findViewById<MaterialToolbar>(R.id.toolbarSettings).setNavigationOnClickListener {
            finish()
        }


    }

    private fun render(state: SettingsState){
        when(state){
            is SettingsState.Share -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        state.text
                    )
                }
                startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
            }

            is SettingsState.Support ->{
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, state.email)
                    putExtra(Intent.EXTRA_SUBJECT, state.subject)
                    putExtra(Intent.EXTRA_TEXT, state.text)
                }
                startActivity(intent)
            }

            is SettingsState.Agreement ->{
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(state.url)
                }
                startActivity(intent)
            }
        }

    }

}