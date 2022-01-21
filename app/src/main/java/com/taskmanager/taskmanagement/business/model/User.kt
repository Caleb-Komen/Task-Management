package com.taskmanager.taskmanagement.business.model

import java.util.*

data class User(
    private val id: String = UUID.randomUUID().toString(),
    private val name: String = "",
    private val username: String = "",
    private val email: String = "",
    private val photo: String = ""
)
