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
    override fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): LiveData<NetworkResult<User>> {
        return userRemoteDataSource.signUpUser(
            name, username, email, password
        )
    }

    override fun signInUser(email: String, password: String): LiveData<NetworkResult<User>> {
        return userRemoteDataSource.signInUser(email, password)
    }

    override suspend fun signOutUser() {
        userRemoteDataSource.signOutUser()
    }

    override suspend fun getUserById(id: String): LiveData<User?> {
        return userRemoteDataSource.getUserById(id)
    }

    override fun getUser(id: String): User? {
        return userRemoteDataSource.getUser(id)
    }

    override suspend fun getUserByName(name: String): List<User?> {
        return userRemoteDataSource.getUserByName(name)
    }

    override suspend fun updateUser(user: User) {
        userRemoteDataSource.updateUser(user)
    }

    override suspend fun deleteUser(id: String) {
        userRemoteDataSource.deleteUser(id)
    }
}