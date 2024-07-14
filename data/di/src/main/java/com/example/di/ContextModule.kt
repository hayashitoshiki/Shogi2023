package com.example.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }
}
