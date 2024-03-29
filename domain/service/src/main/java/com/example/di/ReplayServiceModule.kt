package com.example.di

import com.example.service.ReplayServiceImpl
import com.example.serviceinterface.ReplayService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReplayServiceModule {

    @Binds
    @Singleton
    abstract fun bindGameServiceModule(
        replayServiceImpl: ReplayServiceImpl
    ): ReplayService
}
