package com.taskmanager.taskmanagement.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TaskListWithTasks(
    @Embedded
    val taskListEntity: TaskListEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "task_list_id"
    )
    val taskEntities: List<TaskEntity>
)
