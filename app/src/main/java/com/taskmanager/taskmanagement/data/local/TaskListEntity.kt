package com.taskmanager.taskmanagement.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taskmanager.taskmanagement.business.model.Task
import com.taskmanager.taskmanagement.business.util.Tag
import java.util.*

@Entity(tableName = "taskLists")
data class TaskListEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "tag")
    val tag: String,

    @ColumnInfo(name = "project_id")
    val projectId: String
)
