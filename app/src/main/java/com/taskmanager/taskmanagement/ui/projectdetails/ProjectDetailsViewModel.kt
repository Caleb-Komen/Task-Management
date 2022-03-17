package com.taskmanager.taskmanagement.ui.projectdetails

import androidx.lifecycle.*
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.Status.*
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.usecases.DeleteProjectUseCase
import com.taskmanager.taskmanagement.domain.usecases.GetProjectUseCase
import com.taskmanager.taskmanagement.domain.usecases.InsertTaskListUseCase
import com.taskmanager.taskmanagement.ui.util.DELETE_OK
import com.taskmanager.taskmanagement.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val getProjectUseCase: GetProjectUseCase,
    private val insertTaskListUseCase: InsertTaskListUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val projectId = savedStateHandle.getLiveData<String>(PROJECT_ID_KEY)

    private val _project = projectId.switchMap {
        getProjectData(it)
    }
    val project: LiveData<Project> get() = _project

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> get() = _dataLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> get() = _snackbarText

    private val _deleteProjectEvent = MutableLiveData<Event<Int>>()
    val deleteProjectEvent: LiveData<Event<Int>> get() = _deleteProjectEvent

    fun getProjectData(projectId: String): LiveData<Project>{
        val resource = getProjectUseCase(projectId)
        return resource.asLiveData().distinctUntilChanged().switchMap { res ->
            filterType(res)
        }
    }

    fun createTaskList(taskList: TaskList){
        viewModelScope.launch {
            insertTaskListUseCase(taskList, projectId.value!!)
        }
    }

    fun deleteProject(){
        viewModelScope.launch {
            deleteProjectUseCase(projectId.value!!)
            _deleteProjectEvent.value = Event(DELETE_OK)
        }
    }

    private fun filterType(resource: Resource<Project>?): LiveData<Project>{
        val result = MutableLiveData<Project>()
        resource?.let { res ->
            when (res.status){
                SUCCESS -> {
                    _dataLoading.value = false
                    result.value = res.data!!
                }
                ERROR -> {
                    _dataLoading.value = false
                    _snackbarText.value = Event(res.message!!)
                }
                LOADING -> {
                    _dataLoading.value = true
                }
            }
        }
        return result
    }

    companion object{
        const val PROJECT_ID_KEY = "projectId"
    }
}