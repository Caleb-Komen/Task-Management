package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.data.local.CacheResult
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class GetAllProjectsUseCase(
    private val repository: ProjectRepository
) {
    operator fun invoke(): Flow<CacheResult<List<Project>>>{
        return repository.getAllProjects()
    }
}