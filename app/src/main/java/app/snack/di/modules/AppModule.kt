package app.snack.di.modules

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import app.snack.App
import app.snack.BuildConfig
import app.snack.data.sources.local.Preferences
import app.snack.service.SnackService
import com.amplitude.api.Amplitude
import com.amplitude.api.AmplitudeClient
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.snack.prx.SwipeSdk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setPrettyPrinting()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Singleton
    @Provides
    fun provideProxyInstance(@ApplicationContext context: Context): SwipeSdk {
        SwipeSdk.getInstance(context, BuildConfig.PARTNER_SDK_HASH)
        return SwipeSdk.getInstance(context)
    }

    @Singleton
    @Provides
    @Named("service_intent")
    fun provideServiceIntent(@ApplicationContext context: Context) = Intent(context, SnackService::class.java)



    @Provides
    fun providesAmplitude(@ApplicationContext context: Context, sdk: SwipeSdk): AmplitudeClient {
        return Amplitude.getInstance()
            .initialize(context, BuildConfig.AMPLITUDE_KEY, sdk.deviceUid)
            .enableForegroundTracking(context as App)
    }
}

