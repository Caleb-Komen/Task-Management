package com.taskmanager.taskmanagement.data.repository

import com.taskmanager.taskmanagement.data.local.*
import com.taskmanager.taskmanagement.data.remote.FakeProjectRemoteDataSourceImpl
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteDataSource
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.repository.ProjectRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

class ProjectRepositoryTest {
    private var projectRepository: ProjectRepository
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

    private fun produceHashMapOfProjects(projects: List<Project>): HashMap<String, Project>{
        val map = HashMap<String, Project>()
        for(project in projects){
            map.put(project.id, project)
        }
        return map
    }

    private fun produceHashMapOfUsers(users: List<User>): HashMap<String, User>{
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
        projectRepository = ProjectRepositoryImpl(projectLocalDataSource, projectRemoteDataSource)
    }

    @Test
    fun getAllProjects_success_confirmAllProjectsRetrieved() = runBlocking{
        var projects: ArrayList<Project>? = null
        projectRepository.getAllProjects().collect { resource ->
            resource?.data?.let { list ->
                projects = ArrayList(list)
            }
        }

        // confirm all projects retrieved
        assertTrue{ projects != null }
        val projectsInCache = projectLocalDataSource.getAllProjects()
        assertTrue { projects?.containsAll(projectsInCache) ?: false }
    }

    @Test
    fun getProject_throwException_noResult() = runBlocking{
        val id = FORCE_GET_PROJECT_EXCEPTION
        var project: Project? = null
        projectRepository.getProject(id).collect{ resource ->
            project = resource?.data
        }
        // confirm no result
        assertTrue { project == null }
    }

    @Test
    fun searchProjects_blankQuery_success_confirmProjectsRetrieved() = runBlocking {
        var projects: ArrayList<Project>? = null
        projectRepository.searchProjects("").collect{ resource ->
            resource?.data?.let { list ->
                projects = ArrayList(list)
            }
        }
        // confirm projects were retrieved
        assertTrue{ projects?.size!! > 0 }
        val projectsInCache = projectLocalDataSource.getAllProjects()
        assertTrue { projects?.containsAll(projectsInCache) ?: false }
    }

    @Test
    fun searchProjects_randomQuery_success_confirmNoResults() = runBlocking{
        var projects: ArrayList<Project>? = null
        projectRepository.searchProjects("eydsjwiygcjndcbvui").collect{ resource ->
            resource?.data?.let { list ->
                projects = ArrayList(list)
            }
        }
        // confirm nothing was retrieved
        assertTrue { projects?.size == 0 }

        // confirm projects are in cache
        val projectsInCache = projectLocalDataSource.getAllProjects()
        assertTrue { projectsInCache.isNotEmpty() }
    }

    @Test
    fun searchProjects_fail_confirmNoResults() = runBlocking{
        var projects: ArrayList<Project>? = null
        projectRepository.searchProjects(FORCE_SEARCH_PROJECTS_EXCEPTION).collect{ resource ->
            resource?.data?.let { list ->
                projects = ArrayList(list)
            }
        }
        // confirm no result
        assertTrue { projects == null }

        // confirm projects are in cache
        val projectsInCache = projectLocalDataSource.getAllProjects()
        assertTrue { projectsInCache.isNotEmpty() }
    }

    @Test
    fun createProject_success_confirmCacheAndNetworkUpdated() = runBlocking{
        val project = project1
        projectRepository.createProject(project)

        // confirm project was inserted into local database
        val projectInCache = projectLocalDataSource.getProject(project.id)
        assertTrue { projectInCache == project }

        // confirm project was inserted into the network
        val projectInNetwork = projectRemoteDataSource.getProject(project.id)
        assertTrue { projectInNetwork == project }
    }

    @Test
    fun createProject_fail_confirmCacheAndNetworkUnchanged() = runBlocking{
        val project = Project(
            id = GENERAL_FAILURE,
            name = project1.name,
            taskLists = project1.taskLists,
            members = project1.members
        )
        projectRepository.createProject(project)
        // confirm project was not added into cache
        val projectInCache = projectLocalDataSource.getProject(project.id)
        assertFalse { projectInCache == project }

        // confirm project was not added into the network
        val projectInNetwork = projectRemoteDataSource.getProject(project.id)
        assertFalse { projectInNetwork == project }
    }

    @Test
    fun createProject_throwException_confirmCacheAndNetworkUnchanged() = runBlocking{
        val project = Project(
            id = FORCE_NEW_PROJECT_EXCEPTION,
            name = project1.name,
            taskLists = project1.taskLists,
            members = project1.members
        )
        projectRepository.createProject(project)
        // confirm project was not added into cache
        val projectInCache = projectLocalDataSource.getProject(project.id)
        assertFalse { projectInCache == project }

        // confirm project was not added into the network
        val projectInNetwork = projectRemoteDataSource.getProject(project.id)
        assertFalse { projectInNetwork == project }
    }

    @Test
    fun updateProject_success_confirmCacheAndNetworkUpdated() = runBlocking{
        val project = Project(
            id = project1.id,
            name = "Updated name",
            taskLists = project1.taskLists,
            members = project1.members
        )
        projectRepository.updateProject(project)

        // confirm project was updated in cache
        val projectInCache = projectLocalDataSource.getProject(project.id)
        assertTrue { projectInCache == project }

        // confirm project was updated in network
        val projectInNetwork = projectRemoteDataSource.getProject(project.id)
        assertTrue { projectInNetwork == project }
    }

    @Test
    fun updateProject_fail_confirmCacheAndNetworkUnchanged() = runBlocking{
        // create a project that does not exist
        val project = Project(
            id = UUID.randomUUID().toString(),
            name = UUID.randomUUID().toString(),
            taskLists = emptyList(),
            members = emptyList()
        )
        projectRepository.updateProject(project)

        // confirm project was not updated in cache
        val projectInCache = projectLocalDataSource.getProject(project.id)
        assertTrue { projectInCache != project }

        // confirm project was not updated in network
        val projectInNetwork = projectRemoteDataSource.getProject(project.id)
        assertTrue { projectInNetwork != project }
    }

    @Test
    fun updateProject_throwException_confirmCacheAndNetworkUnchanged() = runBlocking{
        val project = Project(
            id = FORCE_UPDATE_PROJECT_EXCEPTION,
            name = "Updated name",
            taskLists = project1.taskLists,
            members = project1.members
        )
        projectRepository.updateProject(project)

        // confirm project was not updated in cache
        val projectInCache = projectLocalDataSource.getProject(project.id)
        assertTrue { projectInCache != project }

        // confirm project was not updated in network
        val projectInNetwork = projectRemoteDataSource.getProject(project.id)
        assertTrue { projectInNetwork != project }
    }

    @Test
    fun deleteProject_success_confirmCacheNetworkUpdated() = runBlocking{
        val project = project1
        projectRepository.deleteProject(project.id)

        // confirm project was removed from cache
        val projectsInCache = projectLocalDataSource.getAllProjects()
        assert(!projectsInCache.contains(project))

        // confirm project was removed from network
        val projectsInNetwork = projectRemoteDataSource.getAllProjects()
        assert(!projectsInNetwork.contains(project))
    }

    @Test
    fun deleteProject_fail_confirmCacheNetworkUnchanged() = runBlocking{
        val cacheSize = projectLocalDataSource.getAllProjects().size
        val networkSize = projectRemoteDataSource.getAllProjects().size
        // create a project that does not exist
        val project = Project(
            id = UUID.randomUUID().toString(),
            name = UUID.randomUUID().toString(),
            taskLists = emptyList(),
            members = emptyList()
        )
        projectRepository.deleteProject(project.id)

        // confirm cache is unchanged
        val newCacheSize = projectLocalDataSource.getAllProjects().size
        assertTrue { cacheSize == newCacheSize }

        // confirm network is unchanged
        val newNetworkSize = projectRemoteDataSource.getAllProjects().size
        assertTrue { networkSize == newNetworkSize }
    }

    @Test
    fun deleteProject_throwException_confirmCacheNetworkUnchanged() = runBlocking{
        val cacheSize = projectLocalDataSource.getAllProjects().size
        val networkSize = projectRemoteDataSource.getAllProjects().size
        val project = Project(
            id = FORCE_DELETE_PROJECT_EXCEPTION,
            name = UUID.randomUUID().toString(),
            taskLists = emptyList(),
            members = emptyList()
        )
        projectRepository.deleteProject(project.id)

        // confirm cache is unchanged
        val projectsInCache = projectLocalDataSource.getAllProjects()
        assertTrue { cacheSize == projectsInCache.size }

        // confirm network is unchanged
        val projectsInNetwork = projectRemoteDataSource.getAllProjects()
        assertTrue { networkSize == projectsInNetwork.size }
    }
}