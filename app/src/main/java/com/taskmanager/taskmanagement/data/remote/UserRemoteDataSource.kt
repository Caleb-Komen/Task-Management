package com.taskmanager.taskmanagement.data.remote

import androidx.lifecycle.LiveData
import com.taskmanager.taskmanagement.domain.model.User

interface UserRemoteDataSource {
    fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): LiveData<NetworkResult<User>>

    fun signInUser(
        email: String,
        password: String
    ): LiveData<NetworkResult<User>>

    suspend fun signOutUser()

    suspend fun getUserById(id: String): User?

    fun getUser(id: String): User?

    suspend fun getUserByName(name: String): List<User?>

    suspend fun updateUser(user: User)

    suspend fun deleteUser(id: String)
}