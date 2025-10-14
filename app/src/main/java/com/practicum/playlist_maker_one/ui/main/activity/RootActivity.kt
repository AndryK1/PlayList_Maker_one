package com.practicum.playlist_maker_one.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.practicum.playlist_maker_one.R


class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.root_activity)

        val analytics = FirebaseAnalytics.getInstance(this)

        //получаем экземпляр navController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        //получаем экземпляр bottomNavigationView и передаём ему navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        analytics.logEvent("test"){
            param("openedApp", "test")
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.fragmentCreateList ->{
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.fragmentEditPlaylist ->{
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.fragmentCurrentPlaylist ->{
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}