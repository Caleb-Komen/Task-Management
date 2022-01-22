package com.taskmanager.taskmanagement.business.repository

import com.taskmanager.taskmanagement.business.model.Project
import com.taskmanager.taskmanagement.data.local.Result
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getAllProjects(): Flow<Result<List<Project>>>

    fun getProject(): Flow<Result<Project>>

    fun searchProjects(name: String): Flow<Result<List<Project>>>

    suspend fun createProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun deleteProject(id: String)
}