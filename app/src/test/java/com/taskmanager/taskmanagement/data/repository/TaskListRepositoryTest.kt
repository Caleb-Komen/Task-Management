package com.taskmanager.taskmanagement.data.repository

import com.taskmanager.taskmanagement.data.local.*
import com.taskmanager.taskmanagement.data.remote.FakeProjectRemoteDataSourceImpl
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteDataSource
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.TaskListRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class TaskListRepositoryTest {
    private val taskListRepository: TaskListRepository
    private var projectLocalDataSource: ProjectLocalDataSource
    private var projectRemoteDataSource: ProjectRemoteDataSource

    private val user1 = User("1", "User1", "Username1", "email1", "photo1")
    private val user2 = User("2", "User2", "Username2", "email2", "photo2")
    private val task1 = Task("1", "Task1", "Description1", "FEATURE", listOf(user1, user2))
    private val task2 = Task("2", "Task2", "Description2", "BUG", listOf(user1, user2))
    private val taskList1 = TaskList("1", "Title1", listOf(task1, task2), "TODO")
    private val taskList2 = TaskList("2", "Title2", listOf(task1, task2), "DONE")
    private val project1 = Project("1", "Name1", listOf(taskList1, taskList2), listOf(user1, user2))
    private val project2 = Project("2", "Name2", listOf(taskList1, taskList2), listOf(user1, user2))

    private fun produceHashMapOfProjects(projects: List<Project>): HashMap<String, Project> {
        val map = HashMap<String, Project>()
        for(project in projects){
            map.put(project.id, project)
        }
        return map
    }

    private fun produceHashMapOfUsers(users: List<User>): HashMap<String, User> {
        val map = HashMap<String, User>()
        for(user in users){
            map.put(user.id, user)
        }
        return map
    }

    init {
        projectLocalDataSource = FakeProjectLocalDataSourceImpl(
            produceHashMapOfProjects(listOf(project1, project2))
        )
        projectRemoteDataSource = FakeProjectRemoteDataSourceImpl(
            produceHashMapOfProjects(listOf(project1, project2)),
            produceHashMapOfUsers(listOf(user1, user2))
        )
        taskListRepository = TaskListRepositoryImpl(projectLocalDataSource, projectRemoteDataSource)
    }

    @Test
    fun createTaskList_success_confirmCacheAndNetworkUpdated() = runBlocking {
        val taskList = TaskList(
            id = UUID.randomUUID().toString(),
            title = "TaskList title",
            tasks = emptyList(),
            tag = "TODO",
        )
        val project = project1
        taskListRepository.createTaskList(taskList, project.id)

        // confirm taskList added to cache
        val taskListsInCache = projectLocalDataSource.getProject(project.id)?.taskLists
        assertTrue{ taskListsInCache?.contains(taskList) ?: false }

        // confirm taskList added to network
        val taskListsInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists
        assertTrue{ taskListsInNetwork?.contains(taskList) ?: false }
    }

    @Test
    fun createTaskList_fail_confirmCacheAndNetworkUnchanged() = runBlocking{
        val taskList = TaskList(
            id = GENERAL_FAILURE,
            title = "TaskList title",
            tasks = emptyList(),
            tag = "TODO",
        )
        val project = projectLocalDataSource.getAllProjects()[0]
        taskListRepository.createTaskList(taskList, project.id)

        // confirm cache unchanged
        val taskListsInCache = projectLocalDataSource.getProject(project.id)?.taskLists
        assertFalse{ taskListsInCache?.contains(taskList) ?: true }

        // confirm network unchanged
        val taskListsInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists
        assertFalse{ taskListsInNetwork?.contains(taskList) ?: true }
    }

    @Test
    fun createTaskList_throwException_confirmCacheAndNetworkUnchanged() = runBlocking{
        val taskList = TaskList(
            id = FORCE_NEW_TASKLIST_EXCEPTION,
            title = "TaskList title",
            tasks = emptyList(),
            tag = "TODO",
        )
        val project = projectLocalDataSource.getAllProjects()[0]
        taskListRepository.createTaskList(taskList, project.id)

        // confirm cache unchanged
        val taskListsInCache = projectLocalDataSource.getProject(project.id)?.taskLists
        assertFalse{ taskListsInCache?.contains(taskList) ?: true }

        // confirm network unchanged
        val taskListsInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists
        assertFalse{ taskListsInNetwork?.contains(taskList) ?: true }
    }

    @Test
    fun updateTaskList_success_confirmCacheAndNetworkUpdated() = runBlocking{
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        val updatedTaskList = TaskList(
            id = taskList.id,
            title = "Updated title",
            tasks = taskList.tasks,
            tag = taskList.tag
        )

        taskListRepository.updateTaskList(updatedTaskList, project.id)

        // confirm taskList is updated in cache
        var taskListInCache: TaskList? = null
        projectLocalDataSource.getTaskLists(project.id).forEach {
            if (it.id == updatedTaskList.id) taskListInCache = it
        }
        assertTrue { taskListInCache == updatedTaskList }

        // confirm taskList is updated in network
        var taskListInNetwork: TaskList? = null
        projectRemoteDataSource.getProject(project.id)?.taskLists?.forEach {
            if (it.id == updatedTaskList.id) taskListInNetwork = it
        }
        assertTrue { taskListInNetwork == updatedTaskList }
    }

    @Test
    fun updateTaskList_fail_confirmCacheAndNetworkUnchanged() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        // create a project that doesn't exist
        val updatedTaskList = TaskList(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            tasks = taskList.tasks,
            tag = taskList.tag
        )

        taskListRepository.updateTaskList(updatedTaskList, project.id)

        // confirm cache is unchanged
        val taskListsInCache = projectLocalDataSource.getTaskLists(project.id)
        assertTrue { !taskListsInCache.contains(updatedTaskList) }

        // confirm network is unchanged
        val taskListsInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists!!
        assertTrue { !taskListsInNetwork.contains(updatedTaskList) }
    }

    @Test
    fun updateTaskList_throwException_confirmCacheAndNetworkUnchanged() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        val updatedTaskList = TaskList(
            id = FORCE_UPDATE_TASKLIST_EXCEPTION,
            title = UUID.randomUUID().toString(),
            tasks = taskList.tasks,
            tag = taskList.tag
        )

        taskListRepository.updateTaskList(updatedTaskList, project.id)

        // confirm cache is unchanged
        val taskListsInCache = projectLocalDataSource.getTaskLists(project.id)
        assertTrue { !taskListsInCache.contains(updatedTaskList) }

        // confirm network is unchanged
        val taskListsInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists!!
        assertTrue { !taskListsInNetwork.contains(updatedTaskList) }
    }

    @Test
    fun deleteTaskList_success_confirmCacheAndNetworkUpdated() = runBlocking{
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskListToDelete = project.taskLists[0]
        taskListRepository.deleteTaskList(taskListToDelete.id, project.id)

        val taskListsInCache = projectLocalDataSource.getProject(project.id)?.taskLists!!
        assertTrue { !taskListsInCache.contains(taskListToDelete) }

        val taskListsInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists!!
        assertTrue { !taskListsInNetwork.contains(taskListToDelete) }
    }

    @Test
    fun deleteTaskList_fail_confirmCacheAndNetworkUnchanged() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskListCacheSize = project.taskLists.size
        val taskListNetworkSize = projectRemoteDataSource.getProject(project.id)?.taskLists?.size

        // create a taskList that doesn't exist
        val taskListToDelete =  TaskList(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            tasks = emptyList(),
            tag = UUID.randomUUID().toString()
        )
        taskListRepository.deleteTaskList(taskListToDelete.id, project.id)

        // confirm cache unchanged
        val cacheSize = projectLocalDataSource.getProject(project.id)?.taskLists?.size
        assertTrue { cacheSize == taskListCacheSize }

        // confirm network unchanged
        val networkSize = projectRemoteDataSource.getProject(project.id)?.taskLists?.size
        assertTrue { networkSize == taskListNetworkSize }

    }
}