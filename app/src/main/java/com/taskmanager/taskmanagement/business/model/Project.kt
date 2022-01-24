package com.taskmanager.taskmanagement.business.model

import java.util.*

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val taskLists: List<TaskList> = emptyList(),
    val members: List<User> = emptyList()
)
