package com.taskmanager.taskmanagement.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithTaskListsAndTasks(
    @Embedded
    val projectEntity: ProjectEntity,

    @Relation(
        entity = TaskListEntity::class,
        parentColumn = "id",
        entityColumn = "project_id"
    )
    val taskListEntities: List<TaskListWithTasks>
)
