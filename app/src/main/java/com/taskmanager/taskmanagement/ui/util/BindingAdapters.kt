package com.taskmanager.taskmanagement.ui.util

import android.view.Gravity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.ui.projectdetails.ProjectDetailsAdapter
import com.taskmanager.taskmanagement.ui.projects.ProjectsAdapter

@BindingAdapter("app:items")
fun setProjects(view: RecyclerView, projects: List<Project>?){
    projects?.let {
        (view.adapter as ProjectsAdapter).submitList(projects)
    }
}

@BindingAdapter("app:items")
fun setTaskLists(view: RecyclerView, taskLists: List<TaskList>?){
    taskLists?.let {
        (view.adapter as ProjectDetailsAdapter).submitList(taskLists)
        view.smoothScrollToPosition(0)
        val snapHelper = GravitySnapHelper(Gravity.START) // viewpager-like behaviour in recyclerview
        snapHelper.attachToRecyclerView(view)
    }
}