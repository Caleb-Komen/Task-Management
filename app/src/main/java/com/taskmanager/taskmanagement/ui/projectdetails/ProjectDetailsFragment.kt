package com.taskmanager.taskmanagement.ui.projectdetails

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.databinding.FragmentProjectDetailsBinding
import com.taskmanager.taskmanagement.databinding.NewProjectDialogBinding
import com.taskmanager.taskmanagement.databinding.NewTaskListDialogBinding
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.ui.util.showSnackbar
import com.ujumbetech.archtask.Event
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProjectDetailsFragment : Fragment() {
    private var _binding: FragmentProjectDetailsBinding? = null
    val binding get() = _binding!!

    private val viewModel: ProjectDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_project_details, container, false)
        _binding = FragmentProjectDetailsBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        lifecycleScope.launch {
            viewModel.getProject().collect{
                binding.project = it
            }
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.dataLoading.collect {
                    binding.show = it
                }
            }
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.snackbarText.collect{ event ->
                    event.getContentIfNotHandled()?.let {
                        Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun setupAdapter() {
        val adapter = ProjectDetailsAdapter {
            showDialog()
        }
        binding.rvTaskLists.adapter = adapter
    }

    private fun showDialog(){
        val view = requireActivity().layoutInflater.inflate(R.layout.new_task_list_dialog, null)
        val binding = NewTaskListDialogBinding.bind(view)
        val dialog = AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_Light_Dialog_Alert)
            .setView(view)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                val taskListTitle = binding.etTitle.text.toString().trim()
                if(taskListTitle.isEmpty()){
                    dialog.dismiss()
                    return@setPositiveButton
                }
                val taskList = TaskList(title = taskListTitle)
                viewModel.createTaskList(taskList)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}