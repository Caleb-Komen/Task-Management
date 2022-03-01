package com.taskmanager.taskmanagement.data.remote

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.User

interface ProjectRemoteDataSource {
    suspend fun getAllProjects(): List<Project>

    suspend fun getProject(projectId: String): Project?

    suspend fun getAssignedMembers(membersId: List<String>): List<User>

    suspend fun insertProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun deleteProject( projectId: String)

}