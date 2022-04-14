package app.snack.workManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import app.snack.AppActivity
import app.snack.BuildConfig
import app.snack.R
import app.snack.service.SnackService
import org.snack.prx.SwipeSdk

class TrafficWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val sdkInstance = SwipeSdk.getInstance(applicationContext, BuildConfig.PARTNER_SDK_HASH)

    companion object {

        val CHANNEL_ID = "Channel1"
        val NOTIFICATION_NAME = "Foreground Notification"
        val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {

        // Do the work here--in this case, upload the images.
        showNotification()
        sdkInstance.setLoggingEnabled(true)
        sdkInstance.start()
        Log.d("TrafficWorker", "started")
        if (isStopped) {
            sdkInstance.stop()
            Log.d("TrafficWorker", "stopped")
        }

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    override fun onStopped() {

        Log.d("TrafficWorker", "stopped")
        super.onStopped()
    }

    private fun showNotification() {


        val intent1 = Intent(applicationContext, AppActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent1, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat
            .Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Snack")
            .setContentText("Snack traffic sharing started!")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                SnackService.CHANNEL_ID,
                SnackService.NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                applicationContext.getSystemService(NotificationManager::class.java) as NotificationManager
            manager.createNotificationChannel(notificationChannel)

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(NOTIFICATION_ID, notification.build())
            }
        }
    }
}

