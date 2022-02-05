package com.taskmanager.taskmanagement.domain.repository

import androidx.lifecycle.LiveData
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import com.taskmanager.taskmanagement.domain.model.User

interface UserRepository {
    suspend fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): LiveData<NetworkResult<User>>

    suspend fun signInUser(
        email: String,
        password: String
    ): LiveData<NetworkResult<User>>

    suspend fun signOutUser()
}