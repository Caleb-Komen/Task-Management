package com.taskmanager.taskmanagement.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null){
            navigateToLogin()
        }
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
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

    fun displayToast(name: String){
        Toast.makeText(requireContext(), "Welcome $name", Toast.LENGTH_SHORT).show()
    }

}