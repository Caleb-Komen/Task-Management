package com.taskmanager.taskmanagement.ui.projects

import androidx.lifecycle.*
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.Status.*
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.usecases.GetAllProjectsUseCase
import com.ujumbetech.archtask.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getAllProjectsUseCase: GetAllProjectsUseCase
): ViewModel() {
    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> get() = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> get() = _snackbarText

    fun getAllProjects(): LiveData<List<Project>>{
        val resources = getAllProjectsUseCase()
        return resources.asLiveData().distinctUntilChanged().switchMap { resource ->
            filterTypes(resource)
        }
    }

    private fun filterTypes(resource: Resource<List<Project>>?): LiveData<List<Project>>{
        val projects = MutableLiveData<List<Project>>()
        resource?.let { res ->
            when (res.status){
                SUCCESS -> {
                    _dataLoading.value = false
                    projects.value = res.data!!
                }
                ERROR -> {
                    _dataLoading.value = false
                    projects.value = emptyList()
                    _snackbarText.value = Event(R.string.loading_projects_error)
                }
                LOADING -> {
                    _dataLoading.value = true
                }
            }
        }
        return projects
    }
}