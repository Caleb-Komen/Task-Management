package com.taskmanager.taskmanagement.data.remote

import com.taskmanager.taskmanagement.domain.model.User

interface UserRemoteDataSource {
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

    suspend fun getUserById(id: String): User?

    fun getUser(id: String): User?

    suspend fun getUserByName(name: String): List<User?>

    suspend fun updateUser(user: User)

    suspend fun deleteUser(id: String)
}