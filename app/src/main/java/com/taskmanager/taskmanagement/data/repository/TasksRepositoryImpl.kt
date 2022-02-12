package com.taskmanager.taskmanagement.data.repository

import com.taskmanager.taskmanagement.data.local.CacheResponseHandler
import com.taskmanager.taskmanagement.data.local.CacheResult
import com.taskmanager.taskmanagement.data.local.ProjectLocalDataSource
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteDataSource
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.safeCacheCall
import com.taskmanager.taskmanagement.data.util.safeNetworkCall
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.repository.TasksRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksRepositoryImpl @Inject constructor(
    private val projectLocalDataSource: ProjectLocalDataSource,
    private val projectRemoteDataSource: ProjectRemoteDataSource
): TasksRepository {
    override fun getTask(taskId: String): Flow<Resource<Task>?> = flow {
        emit(Resource.loading(null))
        val result = safeCacheCall(IO){
            projectLocalDataSource.getTask(taskId)
        }
        val response = object : CacheResponseHandler<Task>(result){
            override suspend fun handleSuccess(data: Task): Resource<Task>? {
                return Resource.success(data)
            }
        }.getResult()
        emit(response)
    }

    override suspend fun createTask(task: Task, taskListId: String, projectId: String) {
        val result = safeCacheCall(IO){
            projectLocalDataSource.createTask(task, taskListId)
        }

        object : CacheResponseHandler<Long>(result) {
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

    override suspend fun updateTask(task: Task, taskListId: String, projectId: String) {
        val result = safeCacheCall(IO){
            projectLocalDataSource.updateTask(task, taskListId)
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

    override suspend fun deleteTask(id: String, projectId: String) {
        val result = safeCacheCall(IO){
            projectLocalDataSource.deleteTask(id)
        }
        object : CacheResponseHandler<Int>(result){
            override suspend fun handleSuccess(data: Int): Resource<Int>? {
                return if (data > 0){
                    projectResponse(projectId)
                    null
                } else {
                    null
                }
            }
        }
    }

    suspend fun projectResponse(projectId: String): suspend () -> Unit{
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

    private suspend fun updateNetwork(project: Project?) {
        safeNetworkCall(IO){
            projectRemoteDataSource.updateProject(project!!)
        }
    }
}