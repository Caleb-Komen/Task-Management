package com.taskmanager.taskmanagement.data.local.dao

import androidx.room.*
import com.taskmanager.taskmanagement.data.local.entity.ProjectLocalEntity
import com.taskmanager.taskmanagement.data.local.entity.ProjectWithTaskListsAndTasks

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): List<ProjectLocalEntity>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    @Transaction
    fun getProject(projectId: String): ProjectWithTaskListsAndTasks

    @Query("SELECT * FROM projects WHERE name LIKE '%' || :key || '%'")
    fun searchProjects(key: String): List<ProjectLocalEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createProject(projectLocalEntity: ProjectLocalEntity): Long

    @Update
    suspend fun updateProject(projectLocalEntity: ProjectLocalEntity): Int

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: String): Int

    @Query("DELETE FROM projects")
    suspend fun deleteAllProjects(): Int
}