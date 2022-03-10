package com.taskmanager.taskmanagement.ui.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.databinding.ListItemNewProjectBinding

class NewProjectItemViewHolder(
    private val binding: ListItemNewProjectBinding,
    private val showDialog: () -> Unit
): RecyclerView.ViewHolder(binding.root){

    init {
        binding.setClickListener {
            showDialog.invoke()
        }
    }


    companion object{
        fun create(parent: ViewGroup, showDialog: () -> Unit): NewProjectItemViewHolder{
            return NewProjectItemViewHolder(
                ListItemNewProjectBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                showDialog
            )
        }
    }
}