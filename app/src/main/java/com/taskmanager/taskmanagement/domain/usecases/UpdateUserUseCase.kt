package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(user: User){
        userRepository.updateUser(user)
    }
}