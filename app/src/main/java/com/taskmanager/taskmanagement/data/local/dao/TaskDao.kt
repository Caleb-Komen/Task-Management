package com.taskmanager.taskmanagement.data.local.dao

import androidx.room.*
import com.taskmanager.taskmanagement.data.local.entity.TaskLocalEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTask(): List<TaskLocalEntity>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTask(taskId: String): TaskLocalEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(taskLocalEntity: TaskLocalEntity): Long

    @Update
    suspend fun updateTask(taskLocalEntity: TaskLocalEntity): Int

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String): Int

}