package com.taskmanager.taskmanagement.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithTaskLists(
    @Embedded
    val projectEntity: ProjectEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "project_id"
    )
    val taskListEntities: List<TaskListEntity>
)
