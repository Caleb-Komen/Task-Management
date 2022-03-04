package com.taskmanager.taskmanagement.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithTaskListsAndTasks(
    @Embedded
    val projectLocalEntity: ProjectLocalEntity,

    @Relation(
        entity = TaskListLocalEntity::class,
        parentColumn = "id",
        entityColumn = "project_id"
    )
    val taskListEntities: List<TaskListWithTasks>
)
