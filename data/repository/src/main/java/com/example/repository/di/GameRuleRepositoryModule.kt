package com.example.repository.di

import com.example.repository.repository.GameRuleRepositoryImpl
import com.example.repository.repositoryinterface.GameRuleRepository
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
