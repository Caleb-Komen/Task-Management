package com.taskmanager.taskmanagement.data.remote

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.User

class FakeProjectRemoteDataSourceImpl(
    private val projectsData: HashMap<String, Project>,
    private val usersData: HashMap<String, User>
): ProjectRemoteDataSource {
    override suspend fun getAllProjects(): List<Project> {
        return projectsData.values.toList()
    }

    override suspend fun getProject(projectId: String): Project? {
        return projectsData[projectId]
    }

    override suspend fun getAssignedMembers(membersId: List<String>): List<User> {
        val users = ArrayList<User>()
        membersId.forEach { id ->
            usersData.values.forEach { user ->
                if (id == user.id){
                    users.add(user)
                }
            }
        }
        return users
    }

    override suspend fun insertProject(project: Project) {
        projectsData[project.id] = project
    }

    override suspend fun updateProject(project: Project) {
        projectsData[project.id]?.let {
            projectsData.put(project.id, project)
        }
    }

    override suspend fun deleteProject(projectId: String) {
        projectsData.remove(projectId)
    }

}