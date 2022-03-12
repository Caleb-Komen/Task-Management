package com.taskmanager.taskmanagement.domain.usecases

import androidx.lifecycle.LiveData
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: String): User? {
        return userRepository.getUserById(id)
    }
}