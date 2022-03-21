package com.taskmanager.taskmanagement.ui.projects

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.databinding.FragmentProjectsBinding
import com.taskmanager.taskmanagement.databinding.NewProjectDialogBinding
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.ui.MainActivity
import com.taskmanager.taskmanagement.ui.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProjectsFragment : Fragment() {
    private val viewModel: ProjectsViewModel by viewModels()

    private val args: ProjectsFragmentArgs by navArgs()

    private val adapter by lazy {
        ProjectsAdapter(viewModel){
            (requireActivity() as MainActivity).showNewProjectDialog()
        }
    }

    private var _binding: FragmentProjectsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_projects, container, false)
        _binding = FragmentProjectsBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupSnackbar()
        setupNavigation()
    }
    override fun onResume() {
        super.onResume()
        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.getAllProjects().observe(viewLifecycleOwner){ projects ->
            adapter.submitList(projects)
        }

        viewModel.dataLoading.observe(viewLifecycleOwner){
            binding.show = it
        }
    }

    private fun setupAdapter() {
        binding.rvProjects.adapter = adapter
    }

    private fun setupSnackbar(){
        view?.showSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        arguments?.let {
            viewModel.showEditResultMessage(args.message)
        }
    }

    private fun setupNavigation(){
        viewModel.openProjectEvent.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { projectId ->
                val action = ProjectsFragmentDirections.actionProjectsFragmentToProjectDetailsFragment(projectId)
                findNavController().navigate(action)
            }
        }
    }

}