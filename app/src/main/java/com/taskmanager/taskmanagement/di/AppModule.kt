package com.taskmanager.taskmanagement.di

import com.taskmanager.taskmanagement.data.local.ProjectLocalDataSource
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteDataSource
import com.taskmanager.taskmanagement.data.remote.UserRemoteDataSource
import com.taskmanager.taskmanagement.data.repository.ProjectRepositoryImpl
import com.taskmanager.taskmanagement.data.repository.TaskListRepositoryImpl
import com.taskmanager.taskmanagement.data.repository.TasksRepositoryImpl
import com.taskmanager.taskmanagement.data.repository.UserRepositoryImpl
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import com.taskmanager.taskmanagement.domain.repository.TaskListRepository
import com.taskmanager.taskmanagement.domain.repository.TasksRepository
import com.taskmanager.taskmanagement.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideProjectRepository(
        projectLocalDataSource: ProjectLocalDataSource,
        projectRemoteDataSource: ProjectRemoteDataSource
    ): ProjectRepository{
        return ProjectRepositoryImpl(projectLocalDataSource, projectRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideTaskListRepository(
        projectLocalDataSource: ProjectLocalDataSource,
        projectRemoteDataSource: ProjectRemoteDataSource
    ): TaskListRepository {
        return TaskListRepositoryImpl(projectLocalDataSource, projectRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideTasksRepository(
        projectLocalDataSource: ProjectLocalDataSource,
        projectRemoteDataSource: ProjectRemoteDataSource
    ): TasksRepository{
        return TasksRepositoryImpl(projectLocalDataSource, projectRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource
    ): UserRepository{
        return UserRepositoryImpl(userRemoteDataSource)
    }
}