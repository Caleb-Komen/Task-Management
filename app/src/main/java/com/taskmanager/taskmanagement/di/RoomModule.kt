package com.taskmanager.taskmanagement.di

import com.taskmanager.taskmanagement.data.local.ProjectDatabase
import com.taskmanager.taskmanagement.data.local.ProjectLocalDataSource
import com.taskmanager.taskmanagement.data.local.ProjectLocalDataSourceImpl
import com.taskmanager.taskmanagement.data.local.dao.ProjectDao
import com.taskmanager.taskmanagement.data.local.dao.TaskDao
import com.taskmanager.taskmanagement.data.local.dao.TaskListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Singleton
    @Provides
    fun provideProjectDao(database: ProjectDatabase): ProjectDao {
        return database.projectDao()
    }

    @Singleton
    @Provides
    fun provideTaskListDao(database: ProjectDatabase): TaskListDao {
        return database.taskListDao()
    }

    @Singleton
    @Provides
    fun provideTaskDao(database: ProjectDatabase): TaskDao {
        return database.taskDao()
    }

    @Singleton
    @Provides
    fun provideProjectLocalDataSource(
        projectDao: ProjectDao,
        taskListDao: TaskListDao,
        taskDao: TaskDao
    ): ProjectLocalDataSource {
        return ProjectLocalDataSourceImpl(projectDao, taskListDao, taskDao)
    }
}