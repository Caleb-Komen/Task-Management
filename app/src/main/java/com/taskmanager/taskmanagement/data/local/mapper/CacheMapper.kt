package com.taskmanager.taskmanagement.data.local.mapper

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.data.local.entity.*
import com.taskmanager.taskmanagement.domain.model.User

fun Task.toEntity(taskListId: String): TaskLocalEntity {
    return TaskLocalEntity(
        id = id,
        title = title,
        description = description,
        label = label,
        assignedTo = assignedTo.map { it.toEntity() },
        dueDate = dueDate,
        taskListId = taskListId
    )
}

fun TaskLocalEntity.toDomain(): Task{
    return Task(
        id = id,
        title = title,
        description = description,
        label = label,
        assignedTo = assignedTo.map { it.toDomain() },
        dueDate = dueDate
    )
}

fun TaskList.toEntity(projectId: String): TaskListLocalEntity {
    return TaskListLocalEntity(
        id = id,
        title = title,
        tag = tag,
        projectId = projectId
    )
}

fun TaskListWithTasks.toDomain(): TaskList{
    return TaskList(
        id = taskListLocalEntity.id,
        title = taskListLocalEntity.title,
        tasks = taskLocalEntities.map { it.toDomain() },
        tag = taskListLocalEntity.tag
    )
}

fun Project.toEntity(): ProjectLocalEntity {
    return ProjectLocalEntity(
        id = id,
        name = name,
        members = members.map { it.toEntity() }
    )
}

fun ProjectWithTaskListsAndTasks.toDomain(): Project{
    return Project(
        id = projectLocalEntity.id,
        name = projectLocalEntity.name,
        taskLists = taskListEntities.map { it.toDomain() },
        members = projectLocalEntity.members.map{ it.toDomain() }
    )
}

fun ProjectLocalEntity.toDomain(): Project{
    return Project(
        id = id,
        name = name,
        members = members.map { it.toDomain() }
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
