package com.taskmanager.taskmanagement.business.model

import com.taskmanager.taskmanagement.business.util.Tag
import java.util.*

data class TaskList(
    private val id: String = UUID.randomUUID().toString(),
    private val title: String = "",
    private val tasks: List<Task> = emptyList(),
    private val tag: String = Tag.NONE.name
)
