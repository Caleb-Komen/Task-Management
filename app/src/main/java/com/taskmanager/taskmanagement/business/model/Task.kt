package com.taskmanager.taskmanagement.business.model

import java.util.*

data class Task(
    private val id: String = UUID.randomUUID().toString(),
    private val title: String = "",
    private val description: String = "",
    private val label: String = "",
    private val assignedTo: List<User> = emptyList(),
    private val dueDate: String = ""
)
