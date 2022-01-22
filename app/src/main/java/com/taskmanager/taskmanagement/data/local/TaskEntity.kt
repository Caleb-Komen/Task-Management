package com.taskmanager.taskmanagement.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
data class TaskEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "label")
    val label: String,

    @ColumnInfo(name = "assigned_to")
    val assignedTo: List<String>,

    @ColumnInfo(name = "due_date")
    val dueDate: String,

    @ColumnInfo(name = "task_list_id")
    val taskListId: String
)
