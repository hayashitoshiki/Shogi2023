package com.example.di

import com.example.repository.GameRepository
import com.example.repository.GameRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class GameRepositoryModule {

    @Binds
    abstract fun bindLogRepositoryModule(
        gameRepository: GameRepositoryImpl
    ): GameRepository
}
