package app.snack.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import app.snack.AppActivity
import app.snack.BuildConfig
import app.snack.R
import org.snack.prx.SwipeSdk

class SnackService : Service() {

    companion object {
        val CHANNEL_ID = "Channel1"
        val NOTIFICATION_NAME = "Foreground Notification"
        val SERVICE_ACTION = "SNACK_SERVICE_ACTION"
        val SERVICE_STATE_EXTRA = "SERVICE_STATE_EXTRA"
        val NOTIFICATION_ID = 1

        @JvmField
        var isAppInForeground: Boolean = false
    }

    private val sdkInstance
        get() = SwipeSdk.getInstance(this)

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SERVICE_ACTION) {

                val intent1 = Intent(context, AppActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_IMMUTABLE)

                val notification = NotificationCompat
                    .Builder(context, CHANNEL_ID)
                    .setContentTitle("Snack")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)

                val newSdkState = intent.getBooleanExtra(SERVICE_STATE_EXTRA, false)
                if (newSdkState) {
                    notification.setContentText("Snack traffic sharing started!")
                    sdkInstance.start()
                    val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    nm.notify(NOTIFICATION_ID, notification.build())
                } else {
                    sdkInstance.stop()
                    isAppInForeground = false
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val intent1 = Intent(this, AppActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setContentTitle("Snack")
            .setContentText("Snack traffic sharing started!")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        SwipeSdk.getInstance(this, BuildConfig.PARTNER_SDK_HASH)
        sdkInstance.start()

        SnackService.isAppInForeground = true
        startForeground(NOTIFICATION_ID, notification)

        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        SnackService.isAppInForeground = false
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(broadcastReceiver, IntentFilter(SERVICE_ACTION))
    }

    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        // we have to check if OS is oreo and above ( >= 8.0)
        // to create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }
}