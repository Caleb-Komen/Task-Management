package com.taskmanager.taskmanagement.ui.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.databinding.ListItemNewProjectBinding
import com.taskmanager.taskmanagement.databinding.ListItemProjectBinding
import com.taskmanager.taskmanagement.domain.model.Project

class ProjectsAdapter: ListAdapter<Project, RecyclerView.ViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.list_item_project){
            ProjectsViewHolder.create(parent)
        } else {
            NewProjectItemViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == R.layout.list_item_project) {
            val project = getItem(position)
            (holder as ProjectsViewHolder).bind(project)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size || currentList.isNullOrEmpty()){
            R.layout.list_item_new_project
        } else {
            R.layout.list_item_project
        }
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

class NewProjectItemViewHolder(private val binding: ListItemNewProjectBinding): RecyclerView.ViewHolder(binding.root){
    companion object{
        fun create(parent: ViewGroup): NewProjectItemViewHolder{
            return NewProjectItemViewHolder(
                ListItemNewProjectBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
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