package com.taskmanager.taskmanagement.domain.repository

interface UserRepository {
    suspend fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    )

    suspend fun signInUser(
        email: String,
        password: String
    )

    suspend fun signOutUser()
}