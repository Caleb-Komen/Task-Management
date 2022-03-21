package com.taskmanager.taskmanagement.ui.projects

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.databinding.ActivityNewProjectDialogBinding
import com.taskmanager.taskmanagement.domain.model.Project
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NewProjectDialog : AppCompatActivity() {
    private val viewModel: ProjectsViewModel by viewModels()

    lateinit var binding: ActivityNewProjectDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewProjectDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvOk.setOnClickListener {
            val projectName = binding.etName.text.toString().trim()
            if(projectName.isEmpty()){
                finish()
                return@setOnClickListener
            }
            val project = Project(name = projectName)
            viewModel.createProject(project)
            finish()
        }
        binding.tvCancel.setOnClickListener {
            finish()
        }
    }
}