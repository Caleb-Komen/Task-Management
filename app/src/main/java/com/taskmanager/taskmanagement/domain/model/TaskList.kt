package com.taskmanager.taskmanagement.domain.model

import com.taskmanager.taskmanagement.domain.util.Tag
import java.util.*

data class TaskList(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val tasks: List<Task> = emptyList(),
    val tag: String = Tag.NONE.name
)
