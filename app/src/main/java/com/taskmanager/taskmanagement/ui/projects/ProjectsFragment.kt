package com.taskmanager.taskmanagement.ui.projects

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.databinding.FragmentProjectsBinding
import com.taskmanager.taskmanagement.databinding.NewProjectDialogBinding
import com.taskmanager.taskmanagement.domain.model.Project
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
        setupNavigation()
    }

    private fun setupAdapter() {
        binding.rvProjects.adapter = ProjectsAdapter(viewModel) {
            showDialog()
        }
    }

    private fun setupSnackbar(){
        view?.showSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation(){
        viewModel.openProjectEvent.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { projectId ->
                val action = ProjectsFragmentDirections.actionProjectsFragmentToProjectDetailsFragment(projectId)
                findNavController().navigate(action)
            }
        }
    }

    private fun showDialog(){
        val view = requireActivity().layoutInflater.inflate(R.layout.new_project_dialog, null)
        val binding = NewProjectDialogBinding.bind(view)
        val dialog = AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_Light_Dialog_Alert)
            .setView(view)
            .setPositiveButton(R.string.ok) { dialog, which ->
                val projectName = binding.etName.text.toString().trim()
                if(projectName.isEmpty()){
                    dialog.dismiss()
                    return@setPositiveButton
                }
                val project = Project(name = projectName)
                viewModel.createProject(project)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.ok) { dialog, which ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }
}