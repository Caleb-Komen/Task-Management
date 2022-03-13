package com.taskmanager.taskmanagement.ui.projectdetails

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.domain.model.TaskList

class ProjectDetailsAdapter(
    private val showDialog: () -> Unit
): ListAdapter<TaskList, RecyclerView.ViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.list_item_task_list -> {
                TaskListsViewHolder.create(parent)
            }
            R.layout.list_item_add_new -> {
                NewTaskListViewHolder.create(parent, showDialog)
            }
            else -> {
                TaskListsViewHolder.create(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == R.layout.list_item_task_list){
            val taskList = getItem(position)
            (holder as TaskListsViewHolder).bind(taskList)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size || currentList.isNullOrEmpty()){
            R.layout.list_item_add_new
        } else {
            R.layout.list_item_task_list
        }
    }
}

private val DIFF_UTIL = object : DiffUtil.ItemCallback<TaskList>(){
    override fun areItemsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
        return oldItem == newItem
    }
}