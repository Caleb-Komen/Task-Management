package com.taskmanager.taskmanagement.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.data.util.Status.*
import com.taskmanager.taskmanagement.databinding.FragmentDashboardBinding
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.ui.MainActivity
import com.taskmanager.taskmanagement.ui.util.showProgress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by viewModels()
    private var _binding: FragmentDashboardBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null){
            navigateToLogin()
        }
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allProjects.observe(viewLifecycleOwner){
            val resource = it ?: return@observe
            updateUI(resource)
        }
        viewModel.userTasks.observe(viewLifecycleOwner){
            val tasks = it ?: return@observe
            binding.apply {
                myTasksSize = tasks.size
                executePendingBindings()
            }
        }
        viewModel.scheduledTasks.observe(viewLifecycleOwner){
            val tasks = it ?: return@observe
            binding.apply {
                myScheduledTasksSize = tasks.size
                executePendingBindings()
            }
        }
    }

    private fun updateUI(resource: Resource<List<Project>>){
        when (resource.status){
            SUCCESS -> {
                binding.progressBar.showProgress(false)
                val projectsSize = resource.data?.size
                binding.projectsSize = projectsSize
            }
            ERROR -> {
                binding.progressBar.showProgress(false)
                displaySnackbar(resource.message!!)
            }
            LOADING -> {
                binding.progressBar.showProgress(true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_sign_out -> {
                viewModel.signOut()
                navigateToLogin()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToLogin() {
        (requireActivity() as MainActivity).navigateToAuthActivity()
    }

    private fun displaySnackbar(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}