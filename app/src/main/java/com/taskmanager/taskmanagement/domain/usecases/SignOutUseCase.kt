package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.UserRepository

class SignOutUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(){
        userRepository.signOutUser()
    }
}