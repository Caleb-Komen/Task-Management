package com.taskmanager.taskmanagement.data.repository

import com.taskmanager.taskmanagement.data.local.CacheResponseHandler
import com.taskmanager.taskmanagement.data.local.ProjectLocalDataSource
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteDataSource
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.safeCacheCall
import com.taskmanager.taskmanagement.data.util.safeNetworkCall
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProjectRepositoryImpl(
    private val projectLocalDataSource: ProjectLocalDataSource,
    private val projectRemoteDataSource: ProjectRemoteDataSource
): ProjectRepository {
    override fun getAllProjects(): Flow<Resource<List<Project>>?> = flow{
        emit(Resource.loading(null))
        val result = safeCacheCall(IO){
            projectLocalDataSource.getAllProjects()
        }
        val response = object : CacheResponseHandler<List<Project>>(result){
            override suspend fun handleSuccess(data: List<Project>): Resource<List<Project>> {
                return if (data.isEmpty()){
                    Resource.success(emptyList())
                } else {
                    Resource.success(data)
                }
            }
        }.getResult()
        emit(response)
    }

    override fun getProject(id: String): Flow<Resource<Project>?> = flow{
        emit(Resource.loading(null))
        val result = safeCacheCall(IO){
            projectLocalDataSource.getProject(id)
        }
        val response = object : CacheResponseHandler<Project>(result) {
            override suspend fun handleSuccess(data: Project): Resource<Project> {
                return Resource.success(data)
            }

        }.getResult()
        emit(response)
    }

    override fun searchProjects(name: String): Flow<Resource<List<Project>>?> = flow{
        emit(Resource.loading(null))
        val result = safeCacheCall(IO){
            projectLocalDataSource.searchProjects(name)
        }
        val response = object : CacheResponseHandler<List<Project>>(result){
            override suspend fun handleSuccess(data: List<Project>): Resource<List<Project>> {
                return if (data.isEmpty()){
                    Resource.success(emptyList())
                } else {
                    Resource.success(data)
                }
            }
        }.getResult()
        emit(response)
    }

    override suspend fun createProject(project: Project) {
        Resource.loading(null)
        val result = safeCacheCall(IO){
            projectLocalDataSource.createProject(project)
        }
        object : CacheResponseHandler<Long>(result) {
            override suspend fun handleSuccess(data: Long): Resource<Long>? {
                return if (data > 0){
                    insertIntoNetwork(project)
                    null
                } else{
                    null
                }
            }
        }.getResult()
    }

    private suspend fun insertIntoNetwork(project: Project) {
        safeNetworkCall(IO){
            projectRemoteDataSource.insertProject(project)
        }
    }

    override suspend fun updateProject(project: Project) {
        Resource.loading(null)
        val result = safeCacheCall(IO){
            projectLocalDataSource.updateProject(project)
        }
        object : CacheResponseHandler<Int>(result) {
            override suspend fun handleSuccess(data: Int): Resource<Int>? {
                return if (data > 0){
                    updateInNetwork(project)
                    null
                } else{
                    null
                }
            }
        }.getResult()
    }

    private suspend fun updateInNetwork(project: Project) {
        safeNetworkCall(IO){
            projectRemoteDataSource.updateProject(project)
        }
    }

    override suspend fun deleteProject(id: String) {
        Resource.loading(null)
        val result = safeCacheCall(IO){
            projectLocalDataSource.deleteProject(id)
        }
        object : CacheResponseHandler<Int>(result) {
            override suspend fun handleSuccess(data: Int): Resource<Int>? {
                return if (data > 0){
                    deleteInNetwork(id)
                    null
                } else{
                    null
                }
            }
        }.getResult()
    }

    private suspend fun deleteInNetwork(id: String) {
        safeNetworkCall(IO){
            projectRemoteDataSource.deleteProject(id)
        }
    }
}