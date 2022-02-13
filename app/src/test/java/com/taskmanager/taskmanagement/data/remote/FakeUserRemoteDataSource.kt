package com.taskmanager.taskmanagement.data.remote

import androidx.lifecycle.LiveData
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.util.AbsentLiveData

class FakeUserRemoteDataSource(
    private val usersData: HashMap<String, User>
): UserRemoteDataSource {
    override suspend fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): LiveData<NetworkResult<User>> {
        return AbsentLiveData.create()
    }

    override suspend fun signInUser(
        email: String,
        password: String
    ): LiveData<NetworkResult<User>> {
        return AbsentLiveData.create()
    }

    override suspend fun signOutUser() {
        // no op
    }

    override suspend fun getUserById(id: String): User? {
        return usersData[id]
    }

    override fun getUser(id: String): User? {
        return usersData[id]
    }

    override suspend fun getUserByName(name: String): List<User?> {
        val users = ArrayList<User?>()
        usersData.values.forEach {
            if (it.name == name){
                users.add(it)
            }
        }
        return users
    }

    override suspend fun updateUser(user: User) {
        usersData[user.id]?.let {
            usersData[user.id] = user
        }
    }

    override suspend fun deleteUser(id: String) {
        usersData.remove(id)
    }
}