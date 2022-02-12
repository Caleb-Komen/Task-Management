package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(name: String): Flow<Resource<List<Project>>?> {
        return repository.searchProjects(name)
    }
}