package com.taskmanager.taskmanagement.ui.projectdetails

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.databinding.FragmentProjectDetailsBinding
import com.taskmanager.taskmanagement.databinding.NewTaskListDialogBinding
import com.taskmanager.taskmanagement.domain.model.TaskList
import dagger.hilt.android.AndroidEntryPoint
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
        setHasOptionsMenu(true)
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

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.deleteProjectEvent.collect{ event ->
                    event.getContentIfNotHandled()?.let {
                        val action = ProjectDetailsFragmentDirections.actionProjectDetailsFragmentToProjectsFragment(it)
                        findNavController().navigate(action)
                    }
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.project_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_delete -> {
                showDeleteDialog()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setupAdapter() {
        val adapter = ProjectDetailsAdapter {
            showCreateNewDialog()
        }
        binding.rvTaskLists.adapter = adapter
    }

    private fun showCreateNewDialog(){
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

    private fun showDeleteDialog(){
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_dialog_title)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                viewModel.deleteProject()
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