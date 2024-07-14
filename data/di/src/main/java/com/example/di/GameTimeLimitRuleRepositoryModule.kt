package com.example.di

import com.example.repository.GameTimeLimitRuleRepository
import com.example.repository.GameTimeLimitRuleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameTimeLimitRuleRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLogRepositoryModule(
        gameTimeLimitRuleRepository: GameTimeLimitRuleRepositoryImpl,
    ): GameTimeLimitRuleRepository
}
