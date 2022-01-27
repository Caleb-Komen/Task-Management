package com.taskmanager.taskmanagement.data.remote.entity

data class TaskListNetworkEntity(
    val id: String,
    val title: String,
    val tasks: List<TaskNetworkEntity>,
    val tag: String
)
