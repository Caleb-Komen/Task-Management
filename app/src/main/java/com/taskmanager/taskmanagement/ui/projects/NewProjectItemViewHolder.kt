package com.taskmanager.taskmanagement.ui.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.databinding.ListItemNewProjectBinding

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