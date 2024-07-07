package com.example.di

import com.example.repository.BoardRepository
import com.example.repository.BoardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class BoardRepositoryModule {

    @Binds
    abstract fun bindLogRepositoryModule(
        boardRepository: BoardRepositoryImpl,
    ): BoardRepository
}
