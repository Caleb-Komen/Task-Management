package com.taskmanager.taskmanagement.ui.projectdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taskmanager.taskmanagement.databinding.ListItemAddNewBinding

class NewTaskListViewHolder(
    private val binding: ListItemAddNewBinding,
    private val showDialog: () -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.setClickListener { view ->
            showDialog.invoke()
        }
    }

    companion object{
        fun create(parent: ViewGroup, showDialog: () -> Unit): NewTaskListViewHolder{
            return NewTaskListViewHolder(
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