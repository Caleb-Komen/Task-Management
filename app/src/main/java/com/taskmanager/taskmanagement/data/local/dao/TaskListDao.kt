package com.taskmanager.taskmanagement.data.local.dao

import androidx.room.*
import com.taskmanager.taskmanagement.data.local.entity.TaskListLocalEntity

@Dao
interface TaskListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createTaskList(taskListLocalEntity: TaskListLocalEntity): Long

    @Update
    suspend fun updateTaskList(taskListLocalEntity: TaskListLocalEntity): Int

    @Query("DELETE FROM taskLists WHERE id = :id")
    suspend fun deleteTaskList(id: String): Int
}