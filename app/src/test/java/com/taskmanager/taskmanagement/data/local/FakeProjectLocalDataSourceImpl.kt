package com.taskmanager.taskmanagement.data.local

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList

const val GENERAL_FAILURE = "GENERAL_FAILURE"
const val FORCE_NEW_PROJECT_EXCEPTION = "FORCE_NEW_PROJECT_EXCEPTION"
const val FORCE_UPDATE_PROJECT_EXCEPTION = "FORCE_UPDATE_PROJECT_EXCEPTION"
const val FORCE_DELETE_PROJECT_EXCEPTION = "FORCE_DELETE_PROJECT_EXCEPTION"
const val FORCE_SEARCH_PROJECTS_EXCEPTION = "FORCE_SEARCH_PROJECTS_EXCEPTION"
const val FORCE_GET_PROJECT_EXCEPTION = "FORCE_GET_PROJECT_EXCEPTION"
const val FORCE_NEW_TASKLIST_EXCEPTION = "FORCE_GET_PROJECT_EXCEPTION"
const val FORCE_UPDATE_TASKLIST_EXCEPTION = "FORCE_UPDATE_TASKLIST_EXCEPTION"

class FakeProjectLocalDataSourceImpl(
    private val projectsData: HashMap<String, Project>
): ProjectLocalDataSource {
    override fun getAllProjects(): List<Project> {
        return ArrayList(projectsData.values)
    }

    override fun getProject(projectId: String): Project? {
        if (projectId == FORCE_GET_PROJECT_EXCEPTION){
            throw Exception("Unable to retrieve data")
        }
        return projectsData[projectId]
    }

    override fun searchProjects(name: String): List<Project> {
        if (name == FORCE_SEARCH_PROJECTS_EXCEPTION){
            throw Exception("Invalid search query")
        }
        var projects = ArrayList<Project>()
        for (project in projectsData.values){
            if (project.name.contains(name, ignoreCase = true)){
                projects.add(project)
            } else if (project.name == ""){
                projects = ArrayList(getAllProjects())
            }
        }
        return projects
    }

    override suspend fun createProject(project: Project): Long {
        if (project.id == FORCE_NEW_PROJECT_EXCEPTION){
            throw Exception("Something went wrong creating the project")
        }
        if (project.id == GENERAL_FAILURE){
            return -1
        }
        projectsData[project.id] = project
        return 1
    }

    override suspend fun updateProject(project: Project): Int {
        if (project.id == FORCE_UPDATE_PROJECT_EXCEPTION){
            throw Exception("Something went wrong while updating the project")
        }
        return projectsData[project.id]?.let {
            projectsData[project.id] = project
            1
        } ?: -1
    }

    override suspend fun deleteProject(id: String): Int {
        if (id == FORCE_DELETE_PROJECT_EXCEPTION){
            throw Exception("Something went wrong deleting the project")
        }
        return projectsData.remove(id)?.let {
            1
        } ?: -1
    }

    override fun getTaskLists(projectId: String): List<TaskList> {
        return projectsData[projectId]?.taskLists ?: emptyList()
    }

    override suspend fun createTaskList(taskList: TaskList, projectId: String): Long {
        if (taskList.id == FORCE_NEW_TASKLIST_EXCEPTION){
            throw Exception("Something went wrong creating the task list")
        }
        if (taskList.id == GENERAL_FAILURE){
            return -1
        }
        val project = getProject(projectId)
        project?.taskLists?.toMutableList()?.add(taskList)
        updateProject(project!!)
        return 1
    }

    override suspend fun updateTaskList(taskList: TaskList, projectId: String): Int {
        if (taskList.id == FORCE_UPDATE_TASKLIST_EXCEPTION){
            throw Exception("Something went wrong updating the task list")
        }
        if (taskList.id == GENERAL_FAILURE){
            return -1
        }

        val project = getProject(projectId)
        var updatedTaskList = emptyTaskList()
        var index = 0
        project?.taskLists?.let {
            for (list in it){
                if (list.id == taskList.id){
                    updatedTaskList = TaskList(
                        id = taskList.id,
                        title = taskList.title,
                        tasks = taskList.tasks,
                        tag = taskList.tag,
                    )
                    index = it.indexOf(list)
                    break
                }
            }
        }
        project?.taskLists?.toMutableList()?.also {
            it.removeAt(index)
            it.add(index, updatedTaskList)
        }
        updateProject(project!!)
        return 1
    }

    override suspend fun deleteTaskList(id: String): Int {
        var successOrFailure = -1
        projectsData.values.let { projects ->
            for (project in projects){
                project.taskLists.toMutableList().also { tList ->
                    tList.forEach { taskList ->
                        if (taskList.id == id){
                            tList.remove(taskList).let {
                                if (it) successOrFailure = 1
                            }
                        }
                    }
                }
                updateProject(project)
                break
            }
        }
        return successOrFailure
    }

    override fun getAllTasks(): List<Task> {
        var tasks: List<Task> = ArrayList()
        projectsData.values.forEach { project ->
            project.taskLists.forEach { taskList ->
                tasks = taskList.tasks
            }
        }
        return tasks
    }

    override fun getTask(taskId: String): Task {
        var task: Task = emptyTask()
        projectsData.values.forEach { project ->
            project.taskLists.forEach { taskList ->
                taskList.tasks.forEach {
                    if (it.id == taskId){
                        task = it
                    }
                }
            }
        }
        return task
    }

    override suspend fun createTask(task: Task, taskListId: String): Long {
        var taskList: TaskList = emptyTaskList()
        projectsData.values.forEach { project ->
            project.taskLists.forEach {
                if (it.id == taskListId){
                    taskList = it
                }
            }
        }
        return taskList.tasks.toMutableList().add(task).let {
            if (it) 1 else -1
        }
    }

    override suspend fun updateTask(task: Task, taskListId: String): Int {
        var taskList: TaskList = emptyTaskList()
        var updatedTask = emptyTask()
        var index = 0
        projectsData.values.forEach { project ->
            project.taskLists.forEach { tList ->
                if (tList.id == taskListId){
                    taskList = tList
                    tList.tasks.forEach {
                        if (it.id == task.id){
                            index = tList.tasks.indexOf(it)
                            updatedTask = Task(
                                id = task.id,
                                title = task.title,
                                description = task.description,
                                label = task.label,
                                assignedTo = task.assignedTo,
                                dueDate = task.dueDate,
                            )
                        }
                    }
                }
            }
        }
        taskList.tasks.toMutableList().also {
            it.removeAt(index)
            it.add(index, updatedTask)
        }
        return if (updatedTask == task) 1 else -1
    }

    override suspend fun deleteAllTasks(): Int {
        getAllTasks().toMutableList().clear()
        return 1
    }

    override suspend fun deleteTask(id: String): Int {
        var successOrFailure = -1
        projectsData.values.forEach { project ->
            project.taskLists.forEach { tList ->
                tList.tasks.toMutableList().remove(getTask(id)).let {
                    if (it) successOrFailure = 1
                }
            }
        }
        return successOrFailure
    }

    fun emptyTask(): Task{
        return Task("","", "", "", emptyList(), "")
    }

    fun emptyTaskList(): TaskList{
        return TaskList("","", emptyList(), "")
    }

    fun emptyProject(): Project{
        return Project("", "", emptyList(), emptyList())
    }
}