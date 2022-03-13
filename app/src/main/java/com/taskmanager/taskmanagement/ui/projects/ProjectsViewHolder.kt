package com.taskmanager.taskmanagement.ui.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.databinding.ListItemProjectBinding
import com.taskmanager.taskmanagement.domain.model.Project

class ProjectsViewHolder(private val binding: ListItemProjectBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(viewModel: ProjectsViewModel, item: Project?){
        item?.let { project ->
            binding.apply {
                viewmodel = viewModel
                projectName = project.name
                projectId = project.id
                executePendingBindings()
            }
        }
    }

    companion object{
        fun create(parent: ViewGroup): ProjectsViewHolder{
            return ProjectsViewHolder(
                ListItemProjectBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}