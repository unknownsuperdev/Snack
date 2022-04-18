package app.snack.workManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.snack.AppActivity
import app.snack.BuildConfig
import app.snack.R
import org.snack.prx.SwipeSdk
import splitties.init.appCtx

class TrafficWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    private val sdkInstance = SwipeSdk.getInstance(applicationContext, BuildConfig.PARTNER_SDK_HASH)

    companion object {

        private var notificationManager: NotificationManager? = null

        val CHANNEL_ID = "Channel1"
        val NOTIFICATION_NAME = "Foreground Notification"
        val NOTIFICATION_ID = 1

        var isNotificationActive = false

        fun cancelNotification() {
            isNotificationActive = false
            notificationManager?.cancel(NOTIFICATION_ID)
        }

        fun showNotification() {

            if (!isNotificationActive) {

                isNotificationActive = true

                val intent1 = Intent(appCtx, AppActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(appCtx, 0, intent1, PendingIntent.FLAG_IMMUTABLE)

                val notification = NotificationCompat
                    .Builder(appCtx, CHANNEL_ID)
                    .setContentTitle("Snack")
                    .setContentText("Snack traffic sharing started!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(
                        CHANNEL_ID,
                        NOTIFICATION_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationManager =
                        appCtx.getSystemService(NotificationManager::class.java) as NotificationManager
                    notificationManager?.createNotificationChannel(notificationChannel)

                    with(NotificationManagerCompat.from(appCtx)) {
                        notify(NOTIFICATION_ID, notification.build())
                    }
                }
            }
        }
    }

    override suspend fun doWork(): Result {

        // Do the work here--in this case, upload the images.
        sdkInstance.setLoggingEnabled(true)
        sdkInstance.start()
        showNotification()

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}

