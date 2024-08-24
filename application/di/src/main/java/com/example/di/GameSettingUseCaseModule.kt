package com.example.di

import com.example.usecase.GameSettingUseCaseImpl
import com.example.usecaseinterface.usecase.GameSettingUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class GameSettingUseCaseModule {

    @Binds
    abstract fun bindAGameUseCaseModule(
        homeUseCase: GameSettingUseCaseImpl,
    ): GameSettingUseCase
}
