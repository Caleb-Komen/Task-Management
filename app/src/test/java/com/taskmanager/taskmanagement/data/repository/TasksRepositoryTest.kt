package com.taskmanager.taskmanagement.data.repository

import com.taskmanager.taskmanagement.data.local.FakeProjectLocalDataSourceImpl
import com.taskmanager.taskmanagement.data.local.ProjectLocalDataSource
import com.taskmanager.taskmanagement.data.remote.FakeProjectRemoteDataSourceImpl
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteDataSource
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.TasksRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

class TasksRepositoryTest {
    private val tasksRepository: TasksRepository
    private var projectLocalDataSource: ProjectLocalDataSource
    private var projectRemoteDataSource: ProjectRemoteDataSource

    private val user1 = User("1", "User1", "Username1", "email1", "photo1")
    private val user2 = User("2", "User2", "Username2", "email2", "photo2")
    private val task1 = Task("1", "Task1", "Description1", "FEATURE", listOf(user1, user2), "2022/02/20")
    private val task2 = Task("2", "Task2", "Description2", "BUG", listOf(user1, user2), "2022/02/20")
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
        tasksRepository = TasksRepositoryImpl(projectLocalDataSource, projectRemoteDataSource)
    }

    @Test
    fun getTask_success_confirmTaskRetrieved() = runBlocking {
        val task = projectLocalDataSource.getAllProjects()[0].taskLists[0].tasks[0]
        var aTask: Task? = null
        tasksRepository.getTask(task.id).collect{
            aTask = it?.data
        }

        assertTrue{ aTask == task }
    }

    @Test
    fun getTask_fail_confirmNoTaskRetrieved() = runBlocking {
        var aTask: Task? = null
        // Retrieve task with unknown id
        tasksRepository.getTask(UUID.randomUUID().toString()).collect{
            aTask = it?.data
        }

        assertTrue{ aTask == null }
    }

    @Test
    fun createTask_success_confirmCacheAndNetworkUpdated() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            label = "FEATURE",
            assignedTo = emptyList(),
            dueDate = "2022/02/20"
        )
        tasksRepository.createTask(task, taskList.id, project.id)

        val tasksInCache = projectLocalDataSource.getTaskLists(project.id).find{
            it.id == taskList.id
        }?.tasks!!
        assertTrue { tasksInCache.contains(task)}

        val tasksInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists?.find {
            it.id == taskList.id
        }?.tasks!!
        assertTrue { tasksInNetwork.contains(task) }

    }

    @Test
    fun createTask_fail_confirmCacheAndNetworkUnchanged() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        // provide unknown taskList
        val taskList = TaskList(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString()
        )
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            label = "FEATURE",
            assignedTo = emptyList(),
            dueDate = "2022/02/20"
        )
        tasksRepository.createTask(task, taskList.id, project.id)

        val tasksInCache = projectLocalDataSource.getTaskLists(project.id).find{
            it.id == taskList.id
        }?.tasks ?: emptyList()
        assertTrue { !tasksInCache.contains(task)}

        val tasksInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists?.find {
            it.id == taskList.id
        }?.tasks ?: emptyList()
        assertTrue { !tasksInNetwork.contains(task) }

    }

    @Test
    fun updateTask_success_confirmCacheAndNetworkUpdated() = runBlocking{
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        val task = taskList.tasks[0]
        val updatedTask = Task(
            id = task.id,
            title = "New title",
            description = UUID.randomUUID().toString()
        )

        tasksRepository.updateTask(updatedTask, taskList.id, project.id)

        val taskInCache = projectLocalDataSource.getTask(updatedTask.id)
        assertTrue { taskInCache == updatedTask }

        val taskInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists?.find {
            it.id == taskList.id
        }?.tasks?.find { it.id == updatedTask.id }
        assertTrue { taskInNetwork == updatedTask }
    }

    @Test
    fun updateTask_fail_confirmCacheAndNetworkUnchanged() = runBlocking{
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        // create a task that doesn't exist
        val updatedTask = Task(
            id = UUID.randomUUID().toString(),
            title = "New title",
            description = UUID.randomUUID().toString()
        )

        tasksRepository.updateTask(updatedTask, taskList.id, project.id)

        val taskInCache = projectLocalDataSource.getProject(project.id)?.taskLists?.find {
            it.id == taskList.id
        }?.tasks?.find { it.id == updatedTask.id }
        assertTrue { taskInCache != updatedTask }

        val taskInNetwork = projectRemoteDataSource.getProject(project.id)?.taskLists?.find {
            it.id == taskList.id
        }?.tasks?.find { it.id == updatedTask.id }
        assertTrue { taskInNetwork != updatedTask }
    }

    @Test
    fun deleteTask_success_confirmTaskDeleted() = runBlocking{
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskToDelete = project.taskLists[0].tasks[0]

        var cacheSize = 0
        projectLocalDataSource.getProject(project.id)?.taskLists?.forEach { taskList ->
            cacheSize += taskList.tasks.size
        }
        var networkSize = 0
        projectRemoteDataSource.getProject(project.id)?.taskLists?.forEach { taskList ->
            networkSize += taskList.tasks.size
        }

        tasksRepository.deleteTask(taskToDelete.id, project.id)

        var tasksCacheSize = 0
        projectLocalDataSource.getProject(project.id)?.taskLists?.forEach { taskList ->
            tasksCacheSize += taskList.tasks.size
        }
        assertTrue { cacheSize != tasksCacheSize }

        var tasksNetworkSize = 0
        projectRemoteDataSource.getProject(project.id)?.taskLists?.forEach { taskList ->
            tasksNetworkSize += taskList.tasks.size
        }
        assertTrue { networkSize != tasksNetworkSize }
    }

    @Test
    fun deleteTask_fail_confirmNoTaskDeleted() = runBlocking{
        val project = projectLocalDataSource.getAllProjects()[0]
        // create unknown task
        val taskToDelete = Task(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString()
        )

        var cacheSize = 0
        projectLocalDataSource.getProject(project.id)?.taskLists?.forEach { taskList ->
            cacheSize += taskList.tasks.size
        }
        var networkSize = 0
        projectRemoteDataSource.getProject(project.id)?.taskLists?.forEach { taskList ->
            networkSize += taskList.tasks.size
        }

        tasksRepository.deleteTask(taskToDelete.id, project.id)

        var tasksCacheSize = 0
        projectLocalDataSource.getProject(project.id)?.taskLists?.forEach { taskList ->
            tasksCacheSize += taskList.tasks.size
        }
        assertTrue { cacheSize == tasksCacheSize }

        var tasksNetworkSize = 0
        projectRemoteDataSource.getProject(project.id)?.taskLists?.forEach { taskList ->
            tasksNetworkSize += taskList.tasks.size
        }
        assertTrue { networkSize == tasksNetworkSize }
    }
}