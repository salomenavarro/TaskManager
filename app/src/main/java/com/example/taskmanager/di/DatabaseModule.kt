package com.example.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.example.taskmanager.data.local.dao.TaskDraftDao
import com.example.taskmanager.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

// di/DatabaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "task_manager_db").build()
    }

    @Provides
    fun provideTaskDraftDao(database: AppDatabase): TaskDraftDao = database.taskDraftDao()
}