package com.example.di

import com.example.usecase.ReplayUseCaseImpl
import com.example.usecaseinterface.usecase.ReplayUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReplayUseCaseModule {

    @Binds
    abstract fun bindAGameUseCaseModule(
        replayUseCase: ReplayUseCaseImpl,
    ): ReplayUseCase
}
