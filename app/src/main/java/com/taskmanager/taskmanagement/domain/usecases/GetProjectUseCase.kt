package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.ProjectRepository

class GetProjectUseCase(
    private val repository: ProjectRepository
) {
    operator fun invoke(id: String){
        repository.getProject(id)
    }
}