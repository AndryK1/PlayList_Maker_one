package com.practicum.playlist_maker_one.ui.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker_one.R
import com.practicum.playlist_maker_one.domain.api.TrackPlayer
import com.practicum.playlist_maker_one.ui.player.PlayerState
import com.practicum.playlist_maker_one.ui.player.view_model.AudioViewModel.Companion.DELAYED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.scope.serviceScope

class MusicService(
) : Service() {

    private lateinit var previewUrl: String
    private lateinit var startTime: String
    var timerJob: Job?= null
    private lateinit var player : TrackPlayer
    private var artistName : String = "not found"
    private var trackName : String = "not found"
    private val binder = MusicBinder()

    private var isAppInForeground = true
    private var isPlaying = false

    private var serviceScope = CoroutineScope(Dispatchers.Main)

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Paused)
    val playerState : StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _timerState = MutableStateFlow<String>("00:00")
    val timerState = _timerState.asStateFlow()

    private val appStateReceiver = object : BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                APP_IN_FOREGROUND ->{
                    isAppInForeground = true
                    changeNotificationVisibility()
                }
                APP_IN_BACKGROUND ->{
                    isAppInForeground = false
                    changeNotificationVisibility()
                }
            }
        }

    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        registerAppStateReceiver()

    }

    private fun registerAppStateReceiver() {
        try {
            unregisterReceiver(appStateReceiver)
        } catch (e: Exception) {
            // Receiver не зареган
        }

        val filter = IntentFilter().apply {
            addAction(APP_IN_FOREGROUND)
            addAction(APP_IN_BACKGROUND)
        }

        ContextCompat.registerReceiver(
            this,
            appStateReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private fun changeNotificationVisibility(){
        if (isPlaying && !isAppInForeground){
            startForegroundService()
        }
        else{
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)

            if (!isPlaying && !isAppInForeground) {
                stopSelf()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        updateFromIntent(intent)

        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateFromIntent(intent)
        registerAppStateReceiver()
        return START_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    private fun updateFromIntent(intent: Intent?) {
        artistName = intent?.getStringExtra(EXTRA_ARTIST_NAME) ?: artistName
        trackName = intent?.getStringExtra(EXTRA_TRACK_NAME) ?: trackName
    }

    fun prepare(previewUrl: String, time: String) {
        this.previewUrl = previewUrl
        this.startTime = time

        player.preparePlayer(previewUrl) {
            _playerState.value = PlayerState.Finished
            _timerState.value = time
        }
    }


    fun initializePlayer(player: TrackPlayer){
        this.player = player
    }

    fun play(){

        player.playbackControl(){
            startTimer()
        }

        isPlaying = player.getCurrentState()

        if (player.getCurrentState()) {
            _playerState.value = PlayerState.Playing

        } else {
            _playerState.value = PlayerState.Paused

        }

        changeNotificationVisibility()

    }

    private fun startTimer(){

        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while(isActive && player.getCurrentState()){
                val currentSeconds = player.getSecondsRemain()
                _timerState.value = formatTime(currentSeconds)
                delay(DELAYED)
            }
        }
    }

    override fun onDestroy() {

        try {
            unregisterReceiver(appStateReceiver)
        }catch (e : Exception){}
        onDestroyPlayer()
        stopSelf()
        super.onDestroy()
    }

    fun onPausePlayer(){
        player.pausePlayer()
        isPlaying = false
        changeNotificationVisibility()
        stopTimer()
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    fun onDestroyPlayer(){
        timerJob?.cancel()
        isPlaying = false
        timerJob = null
        serviceScope.cancel()
        player.destroy()
    }

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            MUSIC_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = MUSIC_CHANNEL_DESCRIPTION

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun startForegroundService() {
        val notification = createServiceNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(SERVICE_NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(SERVICE_NOTIFICATION_ID, notification)
        }
    }

    private fun createServiceNotification() : Notification{
        return NotificationCompat.Builder(this,
            NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle("Playlist Maker")
            .setContentText("$artistName - $trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSilent(true)
            .build()
    }

    inner class MusicBinder() : Binder(){
        fun getService() : MusicService = this@MusicService
    }

    companion object{
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val MUSIC_CHANNEL_NAME = "music service"
        const val MUSIC_CHANNEL_DESCRIPTION = "Service for playing music"
        const val SERVICE_NOTIFICATION_ID = 100
        const val EXTRA_ARTIST_NAME = "artist_name"
        const val EXTRA_TRACK_NAME = "track_name"

        //для контроля состояния
        const val APP_IN_FOREGROUND = "playList_maker.APP_IN_FOREGROUND"
        const val APP_IN_BACKGROUND = "playList_maker.APP_IN_BACKGROUND"
    }
}