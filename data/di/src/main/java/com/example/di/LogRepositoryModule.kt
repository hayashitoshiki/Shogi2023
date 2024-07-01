package com.example.di

import com.example.repository.LogRepository
import com.example.repository.LogRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LogRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLogRepositoryModule(
        logRepository: LogRepositoryImpl,
    ): LogRepository
}
