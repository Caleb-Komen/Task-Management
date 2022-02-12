package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import javax.inject.Inject

class DeleteProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(id: String){
        repository.deleteProject(id)
    }
}