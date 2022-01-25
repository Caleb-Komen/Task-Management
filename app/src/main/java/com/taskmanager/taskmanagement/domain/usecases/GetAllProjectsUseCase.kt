package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.data.local.Result
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class GetAllProjectsUseCase(
    private val repository: ProjectRepository
) {
    operator fun invoke(): Flow<Result<List<Project>>>{
        return repository.getAllProjects()
    }
}