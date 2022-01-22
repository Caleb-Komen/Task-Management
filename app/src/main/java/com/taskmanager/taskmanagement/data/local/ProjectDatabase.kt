package com.taskmanager.taskmanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProjectEntity::class, TaskListEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class ProjectDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    abstract fun taskListDao(): TaskListDao

    abstract fun taskDao(): TaskDao
}