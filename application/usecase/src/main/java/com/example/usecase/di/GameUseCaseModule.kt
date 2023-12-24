package com.example.usecase.di

import com.example.usecase.usecase.GameUseCaseImpl
import com.example.usecase.usecaseinterface.GameUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class GameUseCaseModule {

    @Binds
    abstract fun bindAGameUseCaseModule(
        gameUseCase: GameUseCaseImpl
    ): GameUseCase
}
