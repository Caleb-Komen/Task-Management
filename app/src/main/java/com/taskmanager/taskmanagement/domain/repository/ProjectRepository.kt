package com.taskmanager.taskmanagement.domain.repository

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.data.local.CacheResult
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getAllProjects(): Flow<CacheResult<List<Project>>>

    fun getProject(id: String): Flow<CacheResult<Project>>

    fun searchProjects(name: String): Flow<CacheResult<List<Project>>>

    suspend fun createProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun deleteProject(id: String)
}