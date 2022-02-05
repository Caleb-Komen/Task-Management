package com.taskmanager.taskmanagement.domain.usecases

import androidx.lifecycle.LiveData
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.UserRepository

class SignInUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): LiveData<NetworkResult<User>> {
        return userRepository.signInUser(email, password)
    }
}