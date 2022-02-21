package com.taskmanager.taskmanagement.data.local

import com.taskmanager.taskmanagement.data.DataFactory
import com.taskmanager.taskmanagement.data.local.dao.ProjectDao
import com.taskmanager.taskmanagement.data.local.dao.TaskDao
import com.taskmanager.taskmanagement.data.local.dao.TaskListDao
import com.taskmanager.taskmanagement.data.local.mapper.toDomain
import com.taskmanager.taskmanagement.data.local.mapper.toEntity
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
class ProjectLocalDataSourceTest {
    lateinit var projectLocalDataSource: ProjectLocalDataSource

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var projectDao: ProjectDao

    @Inject
    lateinit var taskListDao: TaskListDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var dataFactory: DataFactory

    @Before
    fun before(){
        hiltRule.inject()
        insertTestData()
        projectLocalDataSource = ProjectLocalDataSourceImpl(
            projectDao, taskListDao, taskDao
        )
    }

    private fun insertTestData() = runBlocking{
        val projects = dataFactory.produceListOfProjects()
        for (project in projects){
            projectDao.createProject(project.toEntity())
            for (taskList in project.taskLists){
                taskListDao.createTaskList(taskList.toEntity(project.id))
                for (task in taskList.tasks){
                    taskDao.insertTask(task.toEntity(taskList.id))
                }
            }
        }
    }

    @Test
    fun getAllProjects(){
        // first confirm projects are in cache
        val projectsInCache = projectLocalDataSource.getAllProjects()
        assertTrue(projectsInCache.isNotEmpty())
    }

    @Test
    fun getProject() {
        // first confirm projects are in cache
        val projects = projectLocalDataSource.getAllProjects()
        assertTrue(projects.isNotEmpty())

        val project = projectDao.getAllProjects()[0].toDomain()
        val projectInCache = projectLocalDataSource.getProject(project.id)
        assertEquals(project.id, projectInCache?.id)
    }

    @Test
    fun searchProject_blankQuery() {
        // confirm projects are in cache
        val projects = projectDao.getAllProjects().map { it.toDomain() }
        assertTrue(projects.isNotEmpty())

        val projectsInCache = projectLocalDataSource.searchProjects("")
        assertTrue(projects.containsAll(projectsInCache))
    }

    @Test
    fun searchProject_randomQuery() {
        // confirm projects are in cache
        val projects = projectDao.getAllProjects().map { it.toDomain() }
        assertTrue(projects.isNotEmpty())

        val projectsInCache = projectLocalDataSource.searchProjects("ufwcbvokehy46wdjfui37yew7843hd")
        assertTrue(projectsInCache.isEmpty())
    }

    @Test
    fun searchProject_validQuery(){
        // confirm projects are in cache
        val projects = projectDao.getAllProjects().map { it.toDomain() }
        assertTrue(projects.isNotEmpty())

        val project = projectDao.getAllProjects()[0].toDomain()
        val projectsInCache = projectLocalDataSource.searchProjects(project.name)
        assertTrue(projectsInCache.isNotEmpty())
    }

    @Test
    fun createProject() = runBlocking{
        val project = Project(
            id = UUID.randomUUID().toString(),
            name = "New Project",
            taskLists = emptyList(),
            members = emptyList()
        )
        projectLocalDataSource.createProject(project)

        val projectInCache = projectLocalDataSource.getProject(project.id)
        assertEquals(project, projectInCache)
    }

    @Test
    fun updateProject() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val updatedProject = Project(
            id = project.id,
            name = "New Title"
        )
        projectLocalDataSource.updateProject(updatedProject)
        val projectInCache = projectLocalDataSource.getProject(updatedProject.id)
        assertTrue(projectInCache?.name == updatedProject.name)
    }

    @Test
    fun deleteProject() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        projectLocalDataSource.deleteProject(project.id)

        val projectsInCache = projectLocalDataSource.getAllProjects()
        assertTrue(!projectsInCache.contains(project))
    }

    @Test
    fun getTaskLists(){
        // confirm there are task lists
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskLists = project.taskLists
        assertTrue(taskLists.isNotEmpty())

        val tLists = projectLocalDataSource.getTaskLists(project.id)
        assertTrue(tLists.isNotEmpty())
    }

    @Test
    fun createTaskLists() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = TaskList(
            id = UUID.randomUUID().toString(),
            title = "New TaskList"
        )
        projectLocalDataSource.createTaskList(taskList, project.id)
        val taskListInCache = projectLocalDataSource.getProject(project.id)?.taskLists!!
        assertTrue(taskListInCache.contains(taskList))
    }

    @Test
    fun updateTaskList() = runBlocking{
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        val updatedTaskList = TaskList(
            id = taskList.id,
            title = "New title"
        )

        projectLocalDataSource.updateTaskList(updatedTaskList, project.id)
        val taskListInCache =
            projectLocalDataSource.getProject(project.id)?.taskLists?.first { it.id == updatedTaskList.id }
        assertEquals(updatedTaskList.title, taskListInCache?.title)
    }

    @Test
    fun deleteTaskList() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        projectLocalDataSource.deleteTaskList(taskList.id)

        val taskListInCache = projectLocalDataSource.getProject(project.id)?.taskLists!!
        assertTrue(!taskListInCache.contains(taskList))
    }

    @Test
    fun getAllTasks(){
        val tasks = projectLocalDataSource.getAllTasks()
        assertTrue(tasks.isNotEmpty())
    }

    @Test
    fun getTask() {
        val task = projectLocalDataSource.getAllProjects()[0].taskLists[0].tasks[0]
        val taskInCache = projectLocalDataSource.getTask(task.id)
        assertEquals(task, taskInCache)
    }

    @Test
    fun createTask() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = "New Title",
            description = "Brand new Task"
        )
        projectLocalDataSource.createTask(task, taskList.id)

        val tasksInCache = projectLocalDataSource.getProject(project.id)?.taskLists?.first { it.id == taskList.id }?.tasks
        assertTrue(tasksInCache?.contains(task) ?: false)
    }

    @Test
    fun updateTask() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        val task = taskList.tasks[0]
        val updatedTask = Task(
            id = task.id,
            title = "New Title"
        )

        projectLocalDataSource.updateTask(updatedTask, taskList.id)

        val taskInCache = projectLocalDataSource.getTask(updatedTask.id)
        assertEquals(updatedTask, taskInCache)
    }

    @Test
    fun deleteTask() = runBlocking {
        val project = projectLocalDataSource.getAllProjects()[0]
        val taskList = project.taskLists[0]
        val task = taskList.tasks[0]

        projectLocalDataSource.deleteTask(task.id)

        val tasksInCache = projectLocalDataSource.getProject(project.id)?.taskLists?.first { it.id == taskList.id }?.tasks
        assertFalse(tasksInCache?.contains(task)!!)
    }
}