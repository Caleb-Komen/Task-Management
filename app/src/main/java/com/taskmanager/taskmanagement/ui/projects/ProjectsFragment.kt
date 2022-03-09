package com.taskmanager.taskmanagement.ui.projects

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.databinding.FragmentProjectsBinding
import com.taskmanager.taskmanagement.ui.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsFragment : Fragment() {
    private val viewModel: ProjectsViewModel by viewModels()

    private var _binding: FragmentProjectsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_projects, container, false)
        _binding = FragmentProjectsBinding.bind(view).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupSnackbar()
    }

    private fun setupAdapter() {
        binding.rvProjects.adapter = ProjectsAdapter()
    }

    private fun setupSnackbar(){
        view?.showSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }
}