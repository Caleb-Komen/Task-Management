package com.taskmanager.taskmanagement.domain.usecases

import androidx.lifecycle.LiveData
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        username: String,
        email: String,
        password: String,
    ): LiveData<NetworkResult<User>>{
        return userRepository.signUpUser(name, username, email, password)
    }
}