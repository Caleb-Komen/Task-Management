package com.taskmanager.taskmanagement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    @Transaction
    fun getAllProjects(): Flow<List<ProjectWithTaskListsAndTasks>>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    @Transaction
    fun getProject(projectId: String): Flow<ProjectWithTaskListsAndTasks>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createProject(projectEntity: ProjectEntity):Long

    @Update
    suspend fun updateProject(projectEntity: ProjectEntity): Int

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: String): Int

    @Query("DELETE FROM projects")
    suspend fun deleteAllProjects(): Int
}