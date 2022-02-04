package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class SearchProjectsUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(name: String): Flow<Resource<List<Project>>?> {
        return repository.searchProjects(name)
    }
}