package com.taskmanager.taskmanagement.data.remote

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User

interface ProjectRemoteDataSource {
    suspend fun getAllProjects(): List<Project>

    suspend fun getProject(projectId: String): Project

    suspend fun getAssignedMembers(membersId: List<String>): List<User>

    suspend fun insertProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun deleteProject( projectId: String)

    suspend fun getUserById(id: String): User?

    fun getUser(id: String): User?

    suspend fun getUserByName(name: String): List<User?>

    suspend fun updateUser(user: User)

    suspend fun deleteUser(id: String)
}