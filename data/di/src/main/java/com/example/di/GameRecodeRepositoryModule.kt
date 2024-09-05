package com.example.di

import com.example.repository.GameRecodeRepository
import com.example.repository.GameRecodeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameRecodeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLogRepositoryModule(
        gameRecodeRepository: GameRecodeRepositoryImpl,
    ): GameRecodeRepository
}
