package com.taskmanager.taskmanagement.data.repository

import com.taskmanager.taskmanagement.data.local.CacheResponseHandler
import com.taskmanager.taskmanagement.data.local.ProjectLocalDataSource
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteDataSource
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.safeCacheCall
import com.taskmanager.taskmanagement.data.util.safeNetworkCall
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.repository.TaskListRepository
import kotlinx.coroutines.Dispatchers.IO

class TaskListRepositoryImpl(
    private val projectLocalDataSource: ProjectLocalDataSource,
    private val projectRemoteDataSource: ProjectRemoteDataSource
): TaskListRepository {
    override suspend fun createTaskList(taskList: TaskList, projectId: String) {
        val result = safeCacheCall(IO){
            projectLocalDataSource.createTaskList(taskList, projectId)
        }
        object : CacheResponseHandler<Long>(result){
            override suspend fun handleSuccess(data: Long): Resource<Long>? {
                return if (data > 0){
                    projectResponse(projectId).invoke()
                    null
                } else {
                    null
                }
            }
        }.getResult()
    }

    override suspend fun updateTaskList(taskList: TaskList, projectId: String) {
        val result = safeCacheCall(IO){
            projectLocalDataSource.updateTaskList(taskList, projectId)
        }
        object : CacheResponseHandler<Int>(result){
            override suspend fun handleSuccess(data: Int): Resource<Int>? {
                return if (data > 0){
                    projectResponse(projectId).invoke()
                    null
                } else {
                    null
                }
            }
        }.getResult()
    }

    override suspend fun deleteTaskList(id: String, projectId: String) {
        val result = safeCacheCall(IO){
            projectLocalDataSource.deleteTaskList(id)
        }

        object : CacheResponseHandler<Int>(result){
            override suspend fun handleSuccess(data: Int): Resource<Int>? {
                return if (data > 0){
                    projectResponse(projectId).invoke()
                    null
                } else {
                    null
                }
            }
        }.getResult()
    }

    private suspend fun projectResponse(projectId: String): suspend () -> Unit{
        return {
            val projectResult = safeCacheCall(IO){
                projectLocalDataSource.getProject(projectId)
            }
            object : CacheResponseHandler<Project>(projectResult){
                override suspend fun handleSuccess(data: Project): Resource<Project>? {
                    updateNetwork(data)
                    return Resource.success(data)
                }

            }.getResult()
        }
    }

    private suspend fun updateNetwork(data: Project?) {
        safeNetworkCall(IO){
            projectRemoteDataSource.updateProject(data!!)
        }
    }
}