package com.taskmanager.taskmanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.taskmanager.taskmanagement.data.local.dao.ProjectDao
import com.taskmanager.taskmanagement.data.local.dao.TaskDao
import com.taskmanager.taskmanagement.data.local.dao.TaskListDao
import com.taskmanager.taskmanagement.data.local.entity.ProjectLocalEntity
import com.taskmanager.taskmanagement.data.local.entity.TaskLocalEntity
import com.taskmanager.taskmanagement.data.local.entity.TaskListLocalEntity

@TypeConverters(Converter::class)
@Database(entities = [ProjectLocalEntity::class, TaskListLocalEntity::class, TaskLocalEntity::class], version = 1, exportSchema = false)
abstract class ProjectDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    abstract fun taskListDao(): TaskListDao

    abstract fun taskDao(): TaskDao

    companion object{
        const val DATABASE_NAME = "projects_db"
    }
}