package com.example.repository.di

import com.example.repository.repository.GameRepositoryImpl
import com.example.repository.repositoryinterface.GameRepository
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
