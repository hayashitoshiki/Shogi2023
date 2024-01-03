package com.example.usecase.di

import com.example.usecase.usecase.HomeUseCaseImpl
import com.example.usecase.usecaseinterface.HomeUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeUseCaseModule {

    @Binds
    abstract fun bindAGameUseCaseModule(
        homeUseCase: HomeUseCaseImpl
    ): HomeUseCase
}
