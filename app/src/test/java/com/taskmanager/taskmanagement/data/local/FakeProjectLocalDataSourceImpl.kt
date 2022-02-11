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
    private val projectsData: HashMap<String, Project>,
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
        val project = projectsData[projectId]
        val tLists = project?.taskLists?.toMutableList()
        tLists?.add(taskList)
        project?.taskLists = tLists as List<TaskList>
        projectsData[projectId] = project
        return 1
    }

    override suspend fun updateTaskList(taskList: TaskList, projectId: String): Int {
        if (taskList.id == FORCE_UPDATE_TASKLIST_EXCEPTION){
            throw Exception("Something went wrong updating the task list")
        }

        var successOrFailure = -1

        projectsData[projectId]?.let {
            val project = projectsData[projectId]
            val taskLists = project?.taskLists?.toMutableList()
            var index = -1
            taskLists?.forEach { tList ->
                if (tList.id == taskList.id) {
                    index = taskLists.indexOf(tList)
                    successOrFailure = 1
                }
            }
            taskLists?.removeAt(index)
            taskLists?.add(index, taskList)
            project?.taskLists = taskLists as List<TaskList>
            projectsData[projectId] = project
        }
        return successOrFailure
    }

    override suspend fun deleteTaskList(id: String): Int {
        var successOrFailure = -1
        for (project in projectsData.values){
            val taskLists = project.taskLists.toMutableList()
            var index = -1
            for (tList in taskLists){
                if (tList.id == id){
                    index = taskLists.indexOf(tList)
                    successOrFailure = 1
                    break
                }
            }
            taskLists.removeAt(index)
            project.taskLists = taskLists
            projectsData[project.id] = project
            break
        }
        return successOrFailure
    }

    override fun getAllTasks(): List<Task> {
        val tasks: ArrayList<Task> = ArrayList()
        projectsData.values.forEach { project ->
            project.taskLists.forEach { taskList ->
                taskList.tasks.forEach {
                    tasks.add(it)
                }
            }
        }
        return tasks
    }

    override fun getTask(taskId: String): Task {
        var task: Task? = null
        projectsData.values.forEach { project ->
            project.taskLists.forEach { taskList ->
                taskList.tasks.forEach {
                    if (it.id == taskId){
                        task = it
                    }
                }
            }
        }
        return task!!
    }

    override suspend fun createTask(task: Task, taskListId: String): Long {
        var taskList: TaskList? = null
        var project: Project? = null

        for (proj in projectsData.values){
            for (tList in proj.taskLists){
                if (tList.id == taskListId){
                    taskList = tList
                    project = proj
                    break
                }
            }
            if (project != null && taskList != null){
                break
            }
        }
        if (project == null || taskList == null){
            return -1L
        }

        val tasks = taskList.tasks.toMutableList()
        tasks.add(task)
        taskList.tasks = tasks

        val taskLists = project.taskLists.toMutableList()
        taskLists.add(taskList)
        project.taskLists = taskLists

        return projectsData[project.id]?.let {
            projectsData[project.id] = project
            1
        } ?: -1
    }

    override suspend fun updateTask(task: Task, taskListId: String): Int {
        var project: Project? = null
        var taskList: TaskList? = null
        var updatedTask: Task? = null
        var index = -1
//        for (proj in projectsData.values){
//            for (tList in proj.taskLists){
//                if (tList.id == taskListId){
//                    project = proj
//                    taskList = tList
//                    tList.tasks.forEach {
//                        if (it.id == task.id){
//                            index = tList.tasks.indexOf(it)
//                            updatedTask = task
//                        }
//                    }
//                }
//            }
//        }
        projectsData.values.forEach { proj ->
            proj.taskLists.forEach { tList ->
                if (tList.id == taskListId){
                    project = proj
                    taskList = tList
                    tList.tasks.forEach {
                        if (it.id == task.id){
                            index = tList.tasks.indexOf(it)
                            updatedTask = task
                        }
                    }
                }
            }
        }
        if (index == -1) return -1

        val successFailure = if (updatedTask != null) 1 else -1

        val tasks = taskList?.tasks?.toMutableList()!!
        tasks.removeAt(index)
        tasks.add(index, updatedTask!!)
        taskList?.tasks = tasks

        val taskLists = project?.taskLists?.toMutableList()!!
        taskLists.add(taskList!!)
        project?.taskLists = taskLists

        projectsData[project?.id!!] = project!!
        return successFailure
    }


    override suspend fun deleteTask(id: String): Int {
        var project: Project? = null
        var taskList: TaskList? = null
        var task: Task? = null
        var successOrFailure = -1
        var index = -1
        for (proj in projectsData.values){
            for (tList in proj.taskLists){
                for (tsk in tList.tasks){
                    if (tsk.id == id){
                        project = proj
                        taskList = tList
                        task = tsk
                        index = proj.taskLists.indexOf(tList)
                        break
                    }
                }
                if (taskList != null) break
            }
            if (project != null) break
        }

        if (task == null || index == -1) return -1

        val tasks = taskList?.tasks?.toMutableList()!!
        tasks.remove(task).let {
            successOrFailure = if(it) 1 else -1
        }
        taskList.tasks = tasks

        val taskLists = project?.taskLists?.toMutableList()!!
        taskLists.removeAt(index)
        taskLists.add(index, taskList)
        project.taskLists = taskLists

        projectsData[project.id] = project
        return successOrFailure
    }

}