package app.snack

import android.app.Application
import android.util.Log
import com.amplitude.api.AmplitudeClient
import org.snack.prx.SwipeSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var proxySdk: SwipeSdk

    @Inject
    lateinit var amplitudeClient: AmplitudeClient

    override fun onCreate() {
        super.onCreate()
        Log.d("AAA", ">>> device uid ${proxySdk.deviceUid}")
        amplitudeClient.logEvent("app_launched")
    }
}