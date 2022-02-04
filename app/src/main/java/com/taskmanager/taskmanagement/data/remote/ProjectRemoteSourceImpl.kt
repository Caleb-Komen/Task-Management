package com.taskmanager.taskmanagement.data.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.taskmanager.taskmanagement.data.remote.Constants.PROJECTS_COLLECTION
import com.taskmanager.taskmanagement.data.remote.Constants.USERS_COLLECTION
import com.taskmanager.taskmanagement.data.remote.entity.FirebaseUser
import com.taskmanager.taskmanagement.data.remote.entity.ProjectNetworkEntity
import com.taskmanager.taskmanagement.data.remote.entity.TaskListNetworkEntity
import com.taskmanager.taskmanagement.data.remote.mapper.toDomain
import com.taskmanager.taskmanagement.data.remote.mapper.toEntity
import com.taskmanager.taskmanagement.data.util.log
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User
import kotlinx.coroutines.tasks.await

class ProjectRemoteSourceImpl(
    private val firestore: FirebaseFirestore
): ProjectRemoteDataSource {
    override suspend fun getAllProjects(): List<Project> {
        return firestore.collection(PROJECTS_COLLECTION)
            .get()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
            .toObjects(ProjectNetworkEntity::class.java)
            .map {
                it.toDomain(firestore)
            }
    }

    override suspend fun getProject(projectId: String): Project {
        return firestore.collection(PROJECTS_COLLECTION)
            .document(projectId)
            .get()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
            .toObject(ProjectNetworkEntity::class.java)
            ?.toDomain(firestore) ?: Project(id = projectId, name = "")
    }

    override suspend fun getAssignedMembers(membersId: List<String>): List<User> {
        return firestore.collection(USERS_COLLECTION)
            .whereIn("id", membersId)
            .get()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
            .toObjects(FirebaseUser::class.java)
            .map {
                it.toDomain()
            }
    }

    override suspend fun insertProject(project: Project) {
        val entity = project.toEntity()
        firestore.collection(PROJECTS_COLLECTION)
            .document(entity.id)
            .set(entity)
            .addOnFailureListener {
                log(it.message)
            }
            .await()
    }

    override suspend fun updateProject(project: Project) {
        val entity = project.toEntity()
        firestore.collection(PROJECTS_COLLECTION)
            .document(entity.id)
            .set(entity)
            .addOnFailureListener {
                log(it.message)
            }
            .await()

    }

    override suspend fun deleteProject(projectId: String) {
        firestore.collection(PROJECTS_COLLECTION)
            .document(projectId)
            .delete()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
    }

}