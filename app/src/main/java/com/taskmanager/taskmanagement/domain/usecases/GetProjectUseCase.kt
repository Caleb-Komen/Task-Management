package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(id: String): Flow<Resource<Project>?> {
        return repository.getProject(id)
    }
}