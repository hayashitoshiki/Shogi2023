package com.example.di

import android.content.Context
import com.example.local.prefarence.DataStorePreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataStorePreferenceManagerModule {

    @Provides @Singleton
    fun provideDataStorePreferenceManager(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope,
    ): DataStorePreferenceManager {
        return DataStorePreferenceManager(context, coroutineScope)
    }
}
