package com.taskmanager.taskmanagement.domain.model

import java.util.*

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val photo: String = ""
){
    override fun toString(): String {
        return name
    }
}
