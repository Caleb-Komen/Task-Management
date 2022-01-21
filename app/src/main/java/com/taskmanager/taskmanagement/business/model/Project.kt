package com.taskmanager.taskmanagement.business.model

import java.util.*

data class Project(
    private val id: String = UUID.randomUUID().toString(),
    private val name: String = "",
    private val taskLists: List<TaskList> = emptyList(),
    private val members: List<User> = emptyList()
)
