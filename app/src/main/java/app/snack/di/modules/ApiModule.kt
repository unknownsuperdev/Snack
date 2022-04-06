package app.snack.di.modules

import app.snack.BuildConfig
import app.snack.data.sources.remote.SnackAPI
import com.google.gson.Gson
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient  {
        val okHttpInterceptorLogging = LoggingInterceptor.Builder()
            .setLevel(Level.BODY)
            .log(Platform.INFO)
            .tag("REST")
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(okHttpInterceptorLogging)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(gson: Gson, okHttpClient: OkHttpClient): SnackAPI  = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BuildConfig.BASE_HOST)
        .client(okHttpClient)
        .build()
        .create(SnackAPI::class.java)

}
