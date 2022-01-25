package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository

class UpdateProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(project: Project){
        repository.updateProject(project)
    }
}