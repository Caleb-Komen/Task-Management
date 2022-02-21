package com.taskmanager.taskmanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.taskmanager.taskmanagement.domain.model.User
import java.util.*

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = TaskListEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_list_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
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
    val assignedTo: List<UserLocalEntity>,

    @ColumnInfo(name = "due_date")
    val dueDate: String,

    @ColumnInfo(name = "task_list_id")
    val taskListId: String
)
