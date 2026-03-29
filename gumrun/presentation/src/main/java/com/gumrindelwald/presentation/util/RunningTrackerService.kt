package com.gumrindelwald.presentation.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.gumrindelwald.core.domain.formatted
import com.gumrindelwald.domain.RunningTracker
import com.gumrindelwald.gumapp.core.presentation.designsystem.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class RunningTrackerService() : Service() {
    private val notiManager by lazy {
        getSystemService<NotificationManager>()!!
    }

    var serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val runningTracker by inject<RunningTracker>()

    // By the time the noti is created, the app scope might not yet available. We need to put this as lazy
    private val noti by lazy(LazyThreadSafetyMode.NONE) {
        NotificationCompat.Builder(this, NOTI_CHANNEL_ID).setSmallIcon(R.drawable.run)
            .setContentTitle(getString(R.string.gumrun))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isTracking = true

        when (intent?.action) {
            ACTION_START -> {
                Log.d("RunningTrackerService", "Starting service ${intent.extras}")
                val className = intent.getStringExtra(ACTIVITY_CLASSS)!!

                Log.d("RunningTrackerService", "Starting service, $className")
                startNotiService(Class.forName(className) as Class<*>)
            }

            ACTION_STOP -> {
                stopService()
            }
        }

        return START_STICKY
    }

    private fun startNotiService(activityClass: Class<*>) {
        createLocalNotiChannel()

        val activityIntent = Intent(applicationContext, activityClass)

        val pendingIntent =
            PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification =
            noti
                .setContentText("00:00:00")
                .setContentIntent(pendingIntent)
                .setSilent(true)
                .build()

        startForeground(NOTI_ID, notification)

        runningTracker.elapsedTime.onEach {
            notiManager.notify(NOTI_ID, noti.setContentText(it.formatted()).build())
        }.launchIn(serviceScope)
    }

    private fun stopService() {
        isTracking = false
        stopSelf()
        serviceScope.cancel()

        serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    private fun createLocalNotiChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notiChannel = NotificationChannel(
                NOTI_CHANNEL_ID, NOTI_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )

            notiManager.createNotificationChannel(notiChannel)
        }
    }

    companion object {
        var isTracking = false

        const val NOTI_ID = 1
        const val NOTI_CHANNEL_ID = "gumapp_gumrun_channel"

        const val NOTI_NAME = "Gumrun"
        const val ACTION_START = "start_gumrun_track"
        const val ACTION_STOP = "stop_gumrun_track"
        const val ACTIVITY_CLASSS = "activity_class"

        fun createStartIntent(context: Context, activityClass: Class<*>): Intent {
            return Intent(context, RunningTrackerService::class.java).apply {
                action = ACTION_START
                putExtra(ACTIVITY_CLASSS, activityClass.name)
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, RunningTrackerService::class.java).apply {
                action = ACTION_STOP
            }
        }


    }
}
