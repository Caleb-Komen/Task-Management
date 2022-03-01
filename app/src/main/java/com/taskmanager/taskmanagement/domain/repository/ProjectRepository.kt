package com.taskmanager.taskmanagement.domain.repository

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getAllProjects(): Flow<Resource<List<Project>>?>

    fun getProject(id: String): Flow<Resource<Project>?>

    fun searchProjects(name: String): Flow<Resource<List<Project>>?>

    suspend fun createProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun deleteProject(id: String)
}