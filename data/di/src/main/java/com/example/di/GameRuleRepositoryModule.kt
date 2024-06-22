package com.example.di

import com.example.repository.GameRuleRepository
import com.example.repository.GameRuleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameRuleRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLogRepositoryModule(
        gameRuleRepository: GameRuleRepositoryImpl
    ): GameRuleRepository
}
