package com.taskmanager.taskmanagement.data.repository

import androidx.lifecycle.LiveData
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import com.taskmanager.taskmanagement.data.remote.UserRemoteDataSource
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): LiveData<NetworkResult<User>> {
        return userRemoteDataSource.signUpUser(
            name, username, email, password
        )
    }

    override suspend fun signInUser(email: String, password: String): LiveData<NetworkResult<User>> {
        return userRemoteDataSource.signInUser(email, password)
    }

    override suspend fun signOutUser() {
        userRemoteDataSource.signOutUser()
    }
}