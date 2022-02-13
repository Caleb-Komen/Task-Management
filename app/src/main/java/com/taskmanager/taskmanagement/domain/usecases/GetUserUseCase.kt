package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: String){
        userRepository.getUserById(id)
    }
}