package com.taskmanager.taskmanagement.ui.projectdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.Status.*
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.usecases.GetProjectUseCase
import com.taskmanager.taskmanagement.domain.usecases.InsertTaskListUseCase
import com.ujumbetech.archtask.Event
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
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val projectId = savedStateHandle.get<String>(PROJECT_ID_KEY)!!

    private val _dataLoading = MutableStateFlow(false)
    val dataLoading: StateFlow<Boolean> get() = _dataLoading

    private val _snackbarText = MutableStateFlow(Event(""))
    val snackbarText: StateFlow<Event<String>> get() = _snackbarText

    fun getProject(): Flow<Project>{
        val resource = getProjectUseCase(projectId)
        return resource.distinctUntilChanged().flatMapLatest { res ->
            filterType(res)
        }
    }

    fun createTaskList(taskList: TaskList){
        viewModelScope.launch {
            insertTaskListUseCase(taskList, projectId)
        }
    }

    private fun filterType(resource: Resource<Project>?): Flow<Project> = flow{
        resource?.let { res ->
            return@flow when (res.status){
                SUCCESS -> {
                    _dataLoading.value = false
                    emit(res.data!!)
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
    }

    companion object{
        const val PROJECT_ID_KEY = "projectId"
    }
}