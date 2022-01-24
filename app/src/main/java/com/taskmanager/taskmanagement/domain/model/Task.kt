package com.taskmanager.taskmanagement.domain.model

import java.util.*

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val label: String = "",
    val assignedTo: List<User> = emptyList(),
    val dueDate: String = ""
)
