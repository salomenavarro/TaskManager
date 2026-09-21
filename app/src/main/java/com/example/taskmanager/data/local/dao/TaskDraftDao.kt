package com.example.taskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.data.local.entity.TaskDraftEntity
import kotlinx.coroutines.flow.Flow

// data/local/dao/TaskDraftDao.kt
@Dao
interface TaskDraftDao {

    @Query("SELECT * FROM task_drafts WHERE ownerId = :ownerId ORDER BY savedAt DESC")
    fun observeDrafts(ownerId: String): Flow<List<TaskDraftEntity>>

    @Insert
    suspend fun insert(draft: TaskDraftEntity): Long

    @Update
    suspend fun update(draft: TaskDraftEntity)

    @Query("DELETE FROM task_drafts WHERE id = :draftId")
    suspend fun delete(draftId: Int)

    @Query("SELECT * FROM task_drafts WHERE id = :draftId")
    suspend fun getById(draftId: Int): TaskDraftEntity?
}