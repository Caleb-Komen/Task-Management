package com.taskmanager.taskmanagement.ui.projectdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.databinding.ListItemTaskListBinding
import com.taskmanager.taskmanagement.domain.model.TaskList

class TaskListsViewHolder(private val binding: ListItemTaskListBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TaskList){
        binding.apply {
            taskList = item
            executePendingBindings()
        }
    }

    companion object{
        fun create(parent: ViewGroup): TaskListsViewHolder{
            return TaskListsViewHolder(
                ListItemTaskListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}