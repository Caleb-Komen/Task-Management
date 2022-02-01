package com.taskmanager.taskmanagement.data.remote

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.taskmanager.taskmanagement.data.remote.entity.FirebaseUser
import com.taskmanager.taskmanagement.data.remote.entity.ProjectNetworkEntity
import com.taskmanager.taskmanagement.data.remote.entity.TaskListNetworkEntity
import com.taskmanager.taskmanagement.data.remote.mapper.toDomain
import com.taskmanager.taskmanagement.data.remote.mapper.toEntity
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User
import kotlinx.coroutines.tasks.await

class ProjectRemoteSourceImpl(
    private val firestore: FirebaseFirestore
): ProjectRemoteDataSource {
    override suspend fun getAllProjects(): List<Project> {
        return firestore.collection("Projects")
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

    override suspend fun getTaskLists(taskListsId: List<String>): List<TaskList> {
        return firestore.collection(TASKS_LISTS_COLLECTION)
            .whereIn("id", taskListsId)
            .get()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
            .toObjects(TaskListNetworkEntity::class.java)
            .map {
                it.toDomain(firestore)
            }
    }

    override fun getTaskList(id: String): TaskList? {
        var taskList: TaskList? = null
        firestore.collection(TASKS_LISTS_COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                taskList = documentSnapshot.toObject(TaskListNetworkEntity::class.java)?.toDomain(firestore)
            }
            .addOnFailureListener {
                log(it.message)
            }
        return taskList
    }

    override suspend fun insertTaskList(taskList: TaskList) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskList(taskList: TaskList) {
        val entity = taskList.toEntity()
        firestore.collection(TASKS_LISTS_COLLECTION)
            .document(entity.id)
            .set(entity)
            .addOnFailureListener {
                log(it.message)
            }
            .await()
    }

    override suspend fun deleteTaskList(taskList: TaskList) {
        val entity = taskList.toEntity()
        firestore.collection(TASKS_LISTS_COLLECTION)
            .document(entity.id)
            .delete()
            .addOnFailureListener {
                log(it.message)
            }
            .await()
    }

    override suspend fun insertTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
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
            .toObject(FirebaseUser::class.java)
            ?.toDomain()
    }

    override fun getUser(id: String): User? {
        var user: User? = null
        firestore.collection(USERS_COLLECTION)
            .document(id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    user = task.result.toObject(FirebaseUser::class.java)?.toDomain()
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
            .toObjects(FirebaseUser::class.java)
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

    companion object{
        const val PROJECTS_COLLECTION = "Projects"
        const val TASKS_LISTS_COLLECTION = "TasksLists"
        const val USERS_COLLECTION = "Users"

        fun log(message: String?){
            message?.let {
                FirebaseCrashlytics.getInstance().log(it)
            }
        }
    }

}