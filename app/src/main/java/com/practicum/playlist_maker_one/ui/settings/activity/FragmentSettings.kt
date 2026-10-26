package com.practicum.playlist_maker_one.ui.settings.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.databinding.FragmentSettingsBinding
import com.practicum.playlist_maker_one.ui.settings.SettingsState
import com.practicum.playlist_maker_one.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSettings : Fragment() {

    private val viewModel : SettingsViewModel by viewModel()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel?.observeState()?.observe(viewLifecycleOwner){
            if(it){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.darkTheme.isChecked = it
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.darkTheme.isChecked = it
            }
        }

        viewModel?.observeSharingState()?.observe(viewLifecycleOwner){
            it ?: return@observe
            viewModel?.onSharingHandled()
            render(it)
        }
        //тёмная тема

        binding.darkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel?.switchDarkTheme(isChecked)
        }

        //
        binding.share.setOnClickListener {
            viewModel?.onShareClicked()
        }

        binding.support.setOnClickListener {
            viewModel?.onSupportClicked()
        }

        binding.usersAgree.setOnClickListener {
            viewModel?.onAgreementClicked()
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
                // SENDTO + mailto: открывает только почтовые клиенты, а не общее меню "поделиться"
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(state.email))
                    putExtra(Intent.EXTRA_SUBJECT, state.subject)
                    putExtra(Intent.EXTRA_TEXT, state.text)
                }
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), getString(R.string.noMailApp), Toast.LENGTH_SHORT).show()
                }
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