package com.taskmanager.taskmanagement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getAllTasks(taskId: String): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createTask(taskEntity: TaskEntity): Long

    @Update
    suspend fun updateTask(taskEntity: TaskEntity): Int

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String): Int

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks(): Int
}