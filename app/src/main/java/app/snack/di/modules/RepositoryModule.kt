package app.snack.di.modules

import app.snack.data.repository.Repository
import app.snack.data.repository.SnackRepository
import app.snack.data.sources.local.Preferences
import app.snack.data.sources.remote.SnackAPI
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        api: SnackAPI,
        preferences: Preferences
    ): Repository = SnackRepository(api, preferences)

}