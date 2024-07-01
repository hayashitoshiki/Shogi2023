package com.example.di

import com.example.usecase.HomeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeUseCaseModule {

    @Binds
    abstract fun bindAGameUseCaseModule(
        homeUseCase: HomeUseCaseImpl,
    ): com.example.usecaseinterface.usecase.HomeUseCase
}
