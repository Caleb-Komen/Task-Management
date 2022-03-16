package com.taskmanager.taskmanagement.ui.projects

import androidx.lifecycle.*
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.Status.*
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.usecases.GetAllProjectsUseCase
import com.taskmanager.taskmanagement.domain.usecases.InsertProjectUseCase
import com.taskmanager.taskmanagement.ui.util.DELETE_OK
import com.taskmanager.taskmanagement.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val insertProjectUseCase: InsertProjectUseCase
): ViewModel() {
    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> get() = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> get() = _snackbarText

    private val _openProjectEvent = MutableLiveData<Event<String>>()
    val openProjectEvent: LiveData<Event<String>> get() = _openProjectEvent

    private var resultMessageShown = false

    fun getAllProjects(): LiveData<List<Project>>{
        val resources = getAllProjectsUseCase()
        return resources.distinctUntilChanged().flatMapLatest { resource ->
            filterTypes(resource)
        }.asLiveData()
    }

    fun createProject(project: Project){
        viewModelScope.launch {
            insertProjectUseCase(project)
        }
    }

    fun openProject(projectId: String){
        _openProjectEvent.value = Event(projectId)
    }

    fun showEditResultMessage(message: Int){
        if (resultMessageShown) return
        when (message) {
            DELETE_OK -> _snackbarText.value = Event(R.string.project_deleted_message)
        }
        resultMessageShown = true
    }

    private fun filterTypes(resource: Resource<List<Project>>?): Flow<List<Project>>{
        val projects = ArrayList<List<Project>>()
        resource?.let { res ->
            when (res.status){
                SUCCESS -> {
                    _dataLoading.value = false
                    projects.add(res.data!!)
                }
                ERROR -> {
                    _dataLoading.value = false
                    _snackbarText.value = Event(R.string.loading_projects_error)
                }
                LOADING -> {
                    _dataLoading.value = true
                }
            }
        }
        return projects.asFlow()
    }
}