package com.taskmanager.taskmanagement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskListDao {
    @Query("SELECT * FROM taskLists")
    @Transaction
    fun getAllTaskLists(): Flow<List<TaskListWithTasks>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createTaskList(taskListEntity: TaskListEntity): Long

    @Update
    suspend fun updateTaskList(taskListEntity: TaskListEntity): Int

    @Query("DELETE FROM taskLists WHERE id = :id")
    suspend fun deleteTaskList(id: String): Int
}