package com.taskmanager.taskmanagement.data.local.mapper

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.data.local.entity.*

fun Task.toEntity(taskListId: String): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        label = label,
        assignedTo = assignedTo,
        dueDate = dueDate,
        taskListId = taskListId
    )
}

fun TaskEntity.toDomain(): Task{
    return Task(
        id = id,
        title = title,
        description = description,
        label = label,
        assignedTo = assignedTo,
        dueDate = dueDate
    )
}

fun TaskList.toEntity(projectId: String): TaskListEntity {
    return TaskListEntity(
        id = id,
        title = title,
        tag = tag,
        projectId = projectId
    )
}

fun TaskListEntity.toDomain(): TaskList{
    return TaskList(
        id = id,
        title = title,
        tag = tag
    )
}

fun TaskListWithTasks.toDomain(): TaskList{
    return TaskList(
        id = taskListEntity.id,
        title = taskListEntity.title,
        tasks = taskEntities.map { it.toDomain() },
        tag = taskListEntity.tag
    )
}

fun Project.toEntity(): ProjectEntity {
    return ProjectEntity(
        id = id,
        name = name,
        members = members
    )
}

fun ProjectWithTaskListsAndTasks.toDomain(): Project{
    return Project(
        id = projectEntity.id,
        name = projectEntity.name,
        taskLists = taskListEntities.map { it.taskListEntity.toDomain() },
        members = projectEntity.members
    )
}

fun ProjectEntity.toDomain(): Project{
    return Project(
        id = id,
        name = name,
        members = members
    )
}
