package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.ProjectRepository

class SearchProjectsUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(name: String){
        repository.searchProjects(name)
    }
}