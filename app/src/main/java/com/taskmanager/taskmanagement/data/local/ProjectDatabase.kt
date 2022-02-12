package com.taskmanager.taskmanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.taskmanager.taskmanagement.data.local.dao.ProjectDao
import com.taskmanager.taskmanagement.data.local.dao.TaskDao
import com.taskmanager.taskmanagement.data.local.dao.TaskListDao
import com.taskmanager.taskmanagement.data.local.entity.ProjectEntity
import com.taskmanager.taskmanagement.data.local.entity.TaskEntity
import com.taskmanager.taskmanagement.data.local.entity.TaskListEntity

@TypeConverters(Converter::class)
@Database(entities = [ProjectEntity::class, TaskListEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class ProjectDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    abstract fun taskListDao(): TaskListDao

    abstract fun taskDao(): TaskDao

    companion object{
        const val DATABASE_NAME = "projects_db"
    }
}