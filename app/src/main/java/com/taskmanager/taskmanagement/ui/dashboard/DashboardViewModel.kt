package com.taskmanager.taskmanagement.ui.dashboard

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.Status.SUCCESS
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.usecases.GetAllProjectsUseCase
import com.taskmanager.taskmanagement.domain.usecases.GetUserUseCase
import com.taskmanager.taskmanagement.domain.usecases.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getUserUseCase: GetUserUseCase,
    firebaseAuth: FirebaseAuth
): ViewModel() {
    private val _retrieveProjects = MutableLiveData(false)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    val allProjects: LiveData<Resource<List<Project>>?> = _retrieveProjects.switchMap {
        getAllProjectsUseCase().asLiveData()
    }

    val userTasks: LiveData<List<Task>> = allProjects.switchMap { resource ->
        if (resource?.status == SUCCESS){
            getCurrentUserTasks(resource.data!!)
        } else {
            MutableLiveData(emptyList())
        }
    }

    val scheduledTasks: LiveData<List<Task>> = allProjects.switchMap { resource ->
        if (resource?.status == SUCCESS){
            getCurrentUserScheduledTasks(resource.data!!)
        } else {
            MutableLiveData(emptyList())
        }
    }

    init {
        viewModelScope.launch {
            getUserById(firebaseAuth.currentUser?.uid!!)
        }
        loadProjects(true)
    }

    fun loadProjects(retrieve: Boolean){
        _retrieveProjects.value = retrieve
    }

    private fun getCurrentUserTasks(projects: List<Project>): LiveData<List<Task>>{
        val tasks = ArrayList<Task>()
        for (project in projects){
            for (taskList in project.taskLists){
                taskList.tasks.filter { task ->
                    task.assignedTo.contains(_user.value)
                }.forEach {
                    tasks.add(it)
                }
            }
        }
        return MutableLiveData(tasks)
    }

    fun getCurrentUserScheduledTasks(projects: List<Project>): LiveData<List<Task>>{
        val scheduledTasks = ArrayList<Task>()
        for (project in projects){
            for (taskList in project.taskLists){
                taskList.tasks.filter { task ->
                    task.assignedTo.contains(_user.value)
                }.filter { tsk ->
                    tsk.dueDate.isNotEmpty()
                }.forEach {
                    scheduledTasks.add(it)
                }
            }
        }
        return MutableLiveData(scheduledTasks)
    }

    private suspend fun getUserById(userId: String){
        val currentUser = getUserUseCase(userId)
        _user.value = currentUser!!
    }

    fun signOut(){
        viewModelScope.launch {
            signOutUseCase()
        }
    }
}