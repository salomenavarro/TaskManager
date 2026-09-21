package com.example.taskmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.data.local.dao.TaskDraftDao
import com.example.taskmanager.data.local.entity.TaskDraftEntity


// data/local/database/AppDatabase.kt
@Database(entities = [TaskDraftEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDraftDao(): TaskDraftDao
}