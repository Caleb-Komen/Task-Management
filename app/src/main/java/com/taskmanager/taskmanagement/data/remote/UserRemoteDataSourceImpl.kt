package com.taskmanager.taskmanagement.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.taskmanager.taskmanagement.data.remote.Constants.USERS_COLLECTION
import com.taskmanager.taskmanagement.data.remote.entity.UserNetworkEntity
import com.taskmanager.taskmanagement.data.remote.mapper.toDomain
import com.taskmanager.taskmanagement.data.remote.mapper.toEntity
import com.taskmanager.taskmanagement.data.util.log
import com.taskmanager.taskmanagement.domain.model.User
import kotlinx.coroutines.tasks.await

class UserRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
): UserRemoteDataSource {
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

    override suspend fun getUserById(id: String): User? {
        return firestore.collection(USERS_COLLECTION)
            .document(id)
            .get()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
            .toObject(UserNetworkEntity::class.java)
            ?.toDomain()
    }

    override fun getUser(id: String): User? {
        var user: User? = null
        firestore.collection(USERS_COLLECTION)
            .document(id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    user = task.result.toObject(UserNetworkEntity::class.java)?.toDomain()
                } else {
                    log(task.exception?.message)
                }
            }
        return user
    }

    override suspend fun getUserByName(name: String): List<User?> {
        return firestore.collection(USERS_COLLECTION)
            .whereEqualTo("name", name)
            .get()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
            .toObjects(UserNetworkEntity::class.java)
            .map {
                it.toDomain()
            }
    }

    override suspend fun updateUser(user: User) {
        val entity = user.toEntity()
        firestore.collection(USERS_COLLECTION)
            .document(entity.id)
            .update(
                mapOf(
                    "name" to entity.name,
                    "photo" to entity.photo
                )
            )
            .addOnFailureListener {
                log(it.message)
            }
            .await()
    }

    override suspend fun deleteUser(id: String) {
        firestore.collection(USERS_COLLECTION)
            .document(id)
            .delete()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
    }
}