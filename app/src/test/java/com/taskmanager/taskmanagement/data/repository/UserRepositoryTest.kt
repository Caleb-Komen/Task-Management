package com.taskmanager.taskmanagement.data.repository

import com.taskmanager.taskmanagement.data.remote.FakeUserRemoteDataSource
import com.taskmanager.taskmanagement.data.remote.UserRemoteDataSource
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.HashMap

class UserRepositoryTest {
    private val userRepository: UserRepository
    private val userRemoteDataSource: UserRemoteDataSource

    private val user1 = User("1", "User1", "Username1", "email1", "photo1")
    private val user2 = User("2", "User2", "Username2", "email2", "photo2")

    private fun produceHashMapOfUsers(users: List<User>): HashMap<String, User> {
        val map = HashMap<String, User>()
        for(user in users){
            map.put(user.id, user)
        }
        return map
    }

    init {
        userRemoteDataSource = FakeUserRemoteDataSource(produceHashMapOfUsers(listOf(user1, user2)))
        userRepository = UserRepositoryImpl(userRemoteDataSource)
    }

    @Test
    fun getUserById() = runBlocking {
        val user = user1
        val result = userRepository.getUserById(user.id)
        assertTrue{ result == user }
    }

    @Test
    fun getUser() = runBlocking {
        val user = user1
        val result = userRepository.getUser(user.id)
        assertTrue{ result != null }
    }

    @Test
    fun getUserByName_success_usersRetrieved() = runBlocking{
        val user = user1
        val users = userRepository.getUserByName(user.name)
        assertTrue { users.isNotEmpty() }
    }

    @Test
    fun getUserByName_success_emptyResults() = runBlocking{
        val users = userRepository.getUserByName("")
        assertTrue { users.isEmpty() }
    }

    @Test
    fun updateUser() = runBlocking {
        val user = user1
        val updatedUser = User(
            id = user.id,
            name = "New name",
            username = user.username,
            email = user.email,
            photo = "New photo"
        )
        userRepository.updateUser(updatedUser)
        val result = userRemoteDataSource.getUserById(updatedUser.id)
        assertTrue { result == updatedUser }
    }

    @Test
    fun deleteUser() = runBlocking {
        val userToDelete = user1
        userRepository.deleteUser(userToDelete.id)
        val result = userRemoteDataSource.getUserById(userToDelete.id)
        assertTrue { result == null }
    }
}