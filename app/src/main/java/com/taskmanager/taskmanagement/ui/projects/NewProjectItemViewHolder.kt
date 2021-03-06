package com.taskmanager.taskmanagement.ui.projects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.databinding.ListItemAddNewBinding

class NewProjectItemViewHolder(
    binding: ListItemAddNewBinding,
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
                ListItemAddNewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                showDialog
            )
        }
    }
}