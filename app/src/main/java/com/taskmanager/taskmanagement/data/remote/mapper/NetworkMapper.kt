package com.taskmanager.taskmanagement.data.remote.mapper

import com.taskmanager.taskmanagement.data.local.entity.TaskListEntity
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteSourceImpl
import com.taskmanager.taskmanagement.data.remote.entity.FirebaseUser
import com.taskmanager.taskmanagement.data.remote.entity.ProjectNetworkEntity
import com.taskmanager.taskmanagement.data.remote.entity.TaskListNetworkEntity
import com.taskmanager.taskmanagement.data.remote.entity.TaskNetworkEntity
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User

fun User.toEntity(): FirebaseUser{
    return FirebaseUser(
        id = id,
        name = name,
        username = username,
        email = email,
        photo = photo
    )
}

fun FirebaseUser.toDomain(): User{
    return User(
        id = id,
        name = name,
        username = username,
        email = email,
        photo = photo
    )
}

fun Task.toEntity(): TaskNetworkEntity{
    return TaskNetworkEntity(
        id = id,
        title = title,
        description = description,
        label = label,
        assignedTo = assignedTo.map { it.id },
        dueDate = dueDate
    )
}

fun TaskNetworkEntity.toDomain(): Task{
    val source = ProjectRemoteSourceImpl()
    val users = assignedTo.map { id ->
        source.getUser(id)
    }

    return Task(
        id = id,
        title = title,
        description = description,
        label = label,
        assignedTo = users,
        dueDate = dueDate
    )
}

fun TaskList.toEntity(projectId: String): TaskListEntity{
    return TaskListEntity(
        id = id,
        title = title,
        tag = tag,
        projectId = projectId
    )
}

fun TaskListNetworkEntity.toDomain(): TaskList{
    return TaskList(
        id = id,
        title = title,
        tasks = tasks.map { it.toDomain() },
        tag = tag
    )
}

fun Project.toEntity(): ProjectNetworkEntity{
    return ProjectNetworkEntity(
        id = id,
        name = name,
        members = members.map { it.id },
        taskLists = taskLists.map { it.id }
    )
}

fun ProjectNetworkEntity.toDomain(): Project{
    val source = ProjectRemoteSourceImpl()
    return Project(
        id = id,
        name = name,
        taskLists = taskLists.map { id ->
            source.getTaskList(id)
        }
    )
}
