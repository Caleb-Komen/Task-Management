package com.taskmanager.taskmanagement.data.local.dao

import androidx.room.*
import com.taskmanager.taskmanagement.data.local.entity.TaskListEntity

@Dao
interface TaskListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createTaskList(taskListEntity: TaskListEntity): Long

    @Update
    suspend fun updateTaskList(taskListEntity: TaskListEntity): Int

    @Query("DELETE FROM taskLists WHERE id = :id")
    suspend fun deleteTaskList(id: String): Int
}