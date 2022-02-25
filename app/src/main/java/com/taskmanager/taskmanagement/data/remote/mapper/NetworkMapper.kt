package com.taskmanager.taskmanagement.data.remote.mapper

import com.google.firebase.firestore.FirebaseFirestore
import com.taskmanager.taskmanagement.data.remote.UserRemoteDataSourceImpl
import com.taskmanager.taskmanagement.data.remote.entity.UserNetworkEntity
import com.taskmanager.taskmanagement.data.remote.entity.ProjectNetworkEntity
import com.taskmanager.taskmanagement.data.remote.entity.TaskListNetworkEntity
import com.taskmanager.taskmanagement.data.remote.entity.TaskNetworkEntity
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User

fun User.toEntity(): UserNetworkEntity{
    return UserNetworkEntity(
        id = id,
        name = name,
        username = username,
        email = email,
        photo = photo
    )
}

fun UserNetworkEntity.toDomain(): User{
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

fun TaskNetworkEntity.toDomain(firestore: FirebaseFirestore): Task{
    val source = UserRemoteDataSourceImpl(firestore, null)
    val users = assignedTo.map { id ->
        source.getUser(id)
    }

    return Task(
        id = id,
        title = title,
        description = description,
        label = label,
        assignedTo = users as List<User>,
        dueDate = dueDate
    )
}

fun TaskList.toEntity(): TaskListNetworkEntity{
    return TaskListNetworkEntity(
        id = id,
        title = title,
        tag = tag,
        tasks = tasks.map { it.toEntity() }
    )
}

fun TaskListNetworkEntity.toDomain(firestore: FirebaseFirestore): TaskList{
    return TaskList(
        id = id,
        title = title,
        tasks = tasks.map { it.toDomain(firestore) },
        tag = tag
    )
}

fun Project.toEntity(): ProjectNetworkEntity{
    return ProjectNetworkEntity(
        id = id,
        name = name,
        taskLists = taskLists.map { it.toEntity() },
        members = members.map { it.id },
    )
}

fun ProjectNetworkEntity.toDomain(firestore: FirebaseFirestore): Project{
    val source = UserRemoteDataSourceImpl(firestore, null)
    return Project(
        id = id,
        name = name,
        taskLists = taskLists.map {
            it.toDomain(firestore)
        },
        members = members.map {
            source.getUser(it)
        } as List<User>
    )
}

