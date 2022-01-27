package com.taskmanager.taskmanagement.data.remote

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User
import kotlinx.coroutines.flow.Flow

class ProjectRemoteSourceImpl: ProjectRemoteDataSource {
    override suspend fun getAllProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override suspend fun getAssignedMembers(membersId: List<String>): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun insertProject(project: Project) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(project: Project) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskLists(taskListsId: List<String>): List<TaskList> {
        TODO("Not yet implemented")
    }

    override fun getTaskList(id: String): TaskList {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskList(taskList: TaskList) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskList(taskList: TaskList) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: String): User {
        TODO("Not yet implemented")
    }

    override fun getUser(id: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByName(name: String): User {
        TODO("Not yet implemented")
    }

}