package com.example.di

import com.example.usecase.GameUseCaseImpl
import com.example.usecaseinterface.usecase.GameUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class GameUseCaseModule {

    @Binds
    abstract fun bindAGameUseCaseModule(
        gameUseCase: GameUseCaseImpl,
    ): GameUseCase
}
