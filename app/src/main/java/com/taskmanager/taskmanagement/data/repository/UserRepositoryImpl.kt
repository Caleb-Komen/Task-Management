package com.taskmanager.taskmanagement.data.repository

import com.taskmanager.taskmanagement.domain.repository.UserRepository

class UserRepositoryImpl: UserRepository {
    override suspend fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun signInUser(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signOutUser() {
        TODO("Not yet implemented")
    }
}