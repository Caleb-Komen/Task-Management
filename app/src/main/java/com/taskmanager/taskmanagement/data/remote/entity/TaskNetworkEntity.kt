package com.taskmanager.taskmanagement.data.remote.entity

data class TaskNetworkEntity(
    val id: String,
    val title: String,
    val description: String,
    val label: String,
    val assignedTo: List<String>,
    val dueDate: String
){
    constructor(): this("", "", "", "", emptyList(), "")
}
