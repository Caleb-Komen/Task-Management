package com.taskmanager.taskmanagement.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.taskmanager.taskmanagement.R

class DashboardFragment : Fragment() {
//    private val args: DashboardFragmentArgs by navArgs()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val name = args.name
//        if (name != null){
//            showToast(name)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null){
            navigateToLogin()
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    private fun navigateToLogin() {
        val action = DashboardFragmentDirections.actionDashboardFragmentToSignInFragment()
        findNavController().navigate(action)
    }

    // TODO ---> create view extensions for toast and snackbars
    private fun showToast(name: String) {
        Toast.makeText(requireContext(), "Welcome $name", Toast.LENGTH_SHORT).show()
    }

}