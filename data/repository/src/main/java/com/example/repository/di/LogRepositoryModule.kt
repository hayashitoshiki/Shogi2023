package com.example.repository.di

import com.example.repository.repository.LogRepositoryImpl
import com.example.repository.repositoryinterface.LogRepository
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
        logRepository: LogRepositoryImpl
    ): LogRepository
}
