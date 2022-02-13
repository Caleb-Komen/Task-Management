package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String){
        userRepository.getUserByName(name)
    }
}