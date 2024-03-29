package com.example.di

import com.example.service.GameServiceImpl
import com.example.serviceinterface.GameService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameServiceModule {

    @Binds
    @Singleton
    abstract fun bindGameServiceModule(
        gameService: GameServiceImpl
    ): GameService
}
