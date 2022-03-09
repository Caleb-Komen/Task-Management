package com.taskmanager.taskmanagement.ui.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.ui.projects.ProjectsAdapter

@BindingAdapter("app:items")
fun setProjects(view: RecyclerView, projects: List<Project>?){
    projects?.let {
        (view.adapter as ProjectsAdapter).submitList(projects)
    }
}