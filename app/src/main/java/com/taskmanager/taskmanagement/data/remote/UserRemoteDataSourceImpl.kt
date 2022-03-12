package com.taskmanager.taskmanagement.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.taskmanager.taskmanagement.data.remote.Constants.SIGN_IN_FAILED
import com.taskmanager.taskmanagement.data.remote.Constants.SIGN_UP_FAILED
import com.taskmanager.taskmanagement.data.remote.Constants.USERS_COLLECTION
import com.taskmanager.taskmanagement.data.remote.entity.UserNetworkEntity
import com.taskmanager.taskmanagement.data.remote.mapper.toDomain
import com.taskmanager.taskmanagement.data.remote.mapper.toEntity
import com.taskmanager.taskmanagement.data.util.log
import com.taskmanager.taskmanagement.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth?
): UserRemoteDataSource {

    override fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): LiveData<NetworkResult<User>> = liveData{
        val result = MutableLiveData<NetworkResult<User>>()
        try {
            firebaseAuth!!.createUserWithEmailAndPassword(email, password)
                .await()
                .user?.let { firebaseUser ->
                    val userEntity = UserNetworkEntity(
                        id = firebaseUser.uid,
                        name = name,
                        username = username,
                        email = firebaseUser.email!!,
                        photo = ""
                    )
                    val user = userEntity.toDomain()
                    saveUser(userEntity)
                    result.value = NetworkResult.Success(user)
                }
        } catch (e: Exception){
            result.value = NetworkResult.GenericError(null, SIGN_UP_FAILED)
            log(e.message)
        }
        emit(result.value!!)
    }

    override fun signInUser(email: String, password: String): LiveData<NetworkResult<User>> = liveData{
        val result = MutableLiveData<NetworkResult<User>>()
        try {
            firebaseAuth!!.signInWithEmailAndPassword(email, password)
                .await()
                .user.also {
                    val firebaseUser = firebaseAuth.currentUser!!
                    val user = getUserById(firebaseUser.uid)!!
                    result.value = NetworkResult.Success(user)
                }
        } catch (e: Exception){
            result.value = NetworkResult.GenericError(null, SIGN_IN_FAILED)
            log(e.message)
        }
        emit(result.value!!)
    }

    override suspend fun signOutUser() {
        firebaseAuth!!.signOut()
    }

    private suspend fun saveUser(user: UserNetworkEntity) {
        firestore.collection(USERS_COLLECTION)
            .document(user.id)
            .set(user)
            .addOnFailureListener {
                log(it.message)
            }
            .await()
    }

    override suspend fun getUserById(id: String): User?{
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