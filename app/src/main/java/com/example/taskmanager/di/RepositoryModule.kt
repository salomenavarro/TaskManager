package com.example.taskmanager.di

import com.example.taskmanager.data.repository.AuthRepositoryImpl
import com.example.taskmanager.data.repository.DraftRepositoryImpl
import com.example.taskmanager.data.repository.TaskRepositoryImpl
import com.example.taskmanager.domain.repository.AuthRepository
import com.example.taskmanager.domain.repository.DraftRepository
import com.example.taskmanager.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// di/RepositoryModule.kt (actualiza el que ya tenías)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    abstract fun bindDraftRepository(impl: DraftRepositoryImpl): DraftRepository
}