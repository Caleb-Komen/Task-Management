package com.taskmanager.taskmanagement.data.local.mapper

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.data.local.entity.*
import com.taskmanager.taskmanagement.domain.model.User

fun Task.toEntity(taskListId: String): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        label = label,
        assignedTo = assignedTo.map { it.toEntity() },
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
        assignedTo = assignedTo.map { it.toDomain() },
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
        members = members.map { it.toEntity() }
    )
}

fun ProjectWithTaskListsAndTasks.toDomain(): Project{
    return Project(
        id = projectEntity.id,
        name = projectEntity.name,
        taskLists = taskListEntities.map { it.toDomain() },
        members = projectEntity.members.map{ it.toDomain() }
    )
}

fun User.toEntity(): UserLocalEntity{
    return UserLocalEntity(
        id = id,
        name = name,
        username = username,
        email = email,
        photo = photo
    )
}

fun UserLocalEntity.toDomain(): User{
    return User(
        id = id,
        name = name,
        username = username,
        email = email,
        photo = photo
    )
}
