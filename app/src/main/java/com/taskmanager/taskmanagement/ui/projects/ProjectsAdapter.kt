package com.taskmanager.taskmanagement.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.databinding.ListItemProjectBinding
import com.taskmanager.taskmanagement.domain.model.Project

class ProjectsAdapter: ListAdapter<Project, ProjectsViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        return ProjectsViewHolder(
            ListItemProjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        val project = getItem(position)
        holder.bind(project)
    }
}

class ProjectsViewHolder(private val binding: ListItemProjectBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: Project?){
        item?.let { project ->
            binding.apply {
                projectName = project.name
                executePendingBindings()
            }
        }
    }
}

val DIFF_UTIL = object : DiffUtil.ItemCallback<Project>(){
    override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem == newItem
    }
}