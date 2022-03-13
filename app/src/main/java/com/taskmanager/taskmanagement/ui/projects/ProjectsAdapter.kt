package com.taskmanager.taskmanagement.ui.projects

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.domain.model.Project

class ProjectsAdapter(
    private val viewModel: ProjectsViewModel,
    private val showDialog: () -> Unit
): ListAdapter<Project, RecyclerView.ViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.list_item_project){
            ProjectsViewHolder.create(parent)
        } else {
            NewProjectItemViewHolder.create(parent, showDialog)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == R.layout.list_item_project) {
            val project = getItem(position)
            (holder as ProjectsViewHolder).bind(viewModel,project)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size || currentList.isNullOrEmpty()){
            R.layout.list_item_add_new
        } else {
            R.layout.list_item_project
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